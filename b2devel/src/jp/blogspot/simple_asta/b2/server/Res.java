package jp.blogspot.simple_asta.b2.server;

public class Res {
	public final String contents;
	public final String name;
	public final int id;

	public Res(final String id, final String name, final String contents) {
		this.contents = contents;
		this.name = name;
		this.id = id.hashCode();
	}


}
