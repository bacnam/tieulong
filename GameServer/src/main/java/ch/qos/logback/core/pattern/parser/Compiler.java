package ch.qos.logback.core.pattern.parser;

import ch.qos.logback.core.pattern.CompositeConverter;
import ch.qos.logback.core.pattern.Converter;
import ch.qos.logback.core.pattern.DynamicConverter;
import ch.qos.logback.core.pattern.LiteralConverter;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.status.ErrorStatus;
import ch.qos.logback.core.status.Status;
import ch.qos.logback.core.util.OptionHelper;

import java.util.Map;

class Compiler<E>
        extends ContextAwareBase {
    final Node top;
    final Map converterMap;
    Converter<E> head;
    Converter<E> tail;

    Compiler(Node top, Map converterMap) {
        this.top = top;
        this.converterMap = converterMap;
    }

    Converter<E> compile() {
        this.head = this.tail = null;
        for (Node n = this.top; n != null; n = n.next) {
            CompositeNode cn;
            CompositeConverter<E> compositeConverter;
            Compiler<E> childCompiler;
            Converter<E> childConverter;
            SimpleKeywordNode kn;
            DynamicConverter<E> dynaConverter;
            LiteralConverter literalConverter;
            switch (n.type) {
                case 0:
                    addToList((Converter<E>) new LiteralConverter((String) n.getValue()));
                    break;
                case 2:
                    cn = (CompositeNode) n;
                    compositeConverter = createCompositeConverter(cn);
                    if (compositeConverter == null) {
                        addError("Failed to create converter for [%" + cn.getValue() + "] keyword");
                        addToList((Converter<E>) new LiteralConverter("%PARSER_ERROR[" + cn.getValue() + "]"));
                        break;
                    }
                    compositeConverter.setFormattingInfo(cn.getFormatInfo());
                    compositeConverter.setOptionList(cn.getOptions());
                    childCompiler = new Compiler(cn.getChildNode(), this.converterMap);

                    childCompiler.setContext(this.context);
                    childConverter = childCompiler.compile();
                    compositeConverter.setChildConverter(childConverter);
                    addToList((Converter<E>) compositeConverter);
                    break;
                case 1:
                    kn = (SimpleKeywordNode) n;
                    dynaConverter = createConverter(kn);
                    if (dynaConverter != null) {
                        dynaConverter.setFormattingInfo(kn.getFormatInfo());
                        dynaConverter.setOptionList(kn.getOptions());
                        addToList((Converter<E>) dynaConverter);

                        break;
                    }
                    literalConverter = new LiteralConverter("%PARSER_ERROR[" + kn.getValue() + "]");

                    addStatus((Status) new ErrorStatus("[" + kn.getValue() + "] is not a valid conversion word", this));

                    addToList((Converter<E>) literalConverter);
                    break;
            }

        }
        return this.head;
    }

    private void addToList(Converter<E> c) {
        if (this.head == null) {
            this.head = this.tail = c;
        } else {
            this.tail.setNext(c);
            this.tail = c;
        }
    }

    DynamicConverter<E> createConverter(SimpleKeywordNode kn) {
        String keyword = (String) kn.getValue();
        String converterClassStr = (String) this.converterMap.get(keyword);

        if (converterClassStr != null) {
            try {
                return (DynamicConverter<E>) OptionHelper.instantiateByClassName(converterClassStr, DynamicConverter.class, this.context);
            } catch (Exception e) {
                addError("Failed to instantiate converter class [" + converterClassStr + "] for keyword [" + keyword + "]", e);

                return null;
            }
        }
        addError("There is no conversion class registered for conversion word [" + keyword + "]");

        return null;
    }

    CompositeConverter<E> createCompositeConverter(CompositeNode cn) {
        String keyword = (String) cn.getValue();
        String converterClassStr = (String) this.converterMap.get(keyword);

        if (converterClassStr != null) {
            try {
                return (CompositeConverter<E>) OptionHelper.instantiateByClassName(converterClassStr, CompositeConverter.class, this.context);
            } catch (Exception e) {
                addError("Failed to instantiate converter class [" + converterClassStr + "] as a composite converter for keyword [" + keyword + "]", e);

                return null;
            }
        }
        addError("There is no conversion class registered for composite conversion word [" + keyword + "]");

        return null;
    }
}

