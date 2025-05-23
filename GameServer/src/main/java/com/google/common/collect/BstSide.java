package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible
enum BstSide
{
LEFT
{
public BstSide other() {
return RIGHT;
}
},
RIGHT
{
public BstSide other() {
return LEFT;
}
};

abstract BstSide other();
}

