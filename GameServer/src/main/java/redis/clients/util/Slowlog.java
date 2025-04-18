package redis.clients.util;

import java.util.ArrayList;
import java.util.List;

public class Slowlog {
	private final long id;
	private final long timeStamp;
	private final long executionTime;
	private final List<String> args;

	@SuppressWarnings("unchecked")
	public static List<Slowlog> from(List<Object> nestedMultiBulkReply){
		List<Slowlog> logs = new ArrayList<Slowlog>(nestedMultiBulkReply.size());
		for(Object obj : nestedMultiBulkReply){
			List<Object> properties = (List<Object>)obj;
			logs.add(new Slowlog(properties));
		}

		return logs;
	}

	@SuppressWarnings("unchecked")
	private Slowlog(List<Object> properties) {
		super();
		this.id = (Long)properties.get(0);
		this.timeStamp = (Long)properties.get(1);
		this.executionTime = (Long)properties.get(2);

		List<byte[]> bargs = (List<byte[]>)properties.get(3);
		this.args = new ArrayList<String>(bargs.size());

		for(byte[] barg:bargs){
			this.args.add(SafeEncoder.encode(barg));
		}
	}

	public long getId() {
		return id;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public long getExecutionTime() {
		return executionTime;
	}

	public List<String> getArgs() {
		return args;
	}
}
