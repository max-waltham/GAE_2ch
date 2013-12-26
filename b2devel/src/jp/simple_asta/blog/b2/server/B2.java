package jp.simple_asta.blog.b2.server;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

interface ReadLineD {
	public void readLine(String line);
}

class Num {
	int count = 0;

	public void incl() {
		count++;
	}

	public String toString() {
		return Integer.toString(count + 1);
	}
}

public class B2 {
	public static final int TYPE_CATEGORY = "category".hashCode();
	public static final int TYPE_THREADLIST = "threadlist".hashCode();
	public static final int TYPE_THREAD = "thread".hashCode();

	private static final String bbsmenu = "http://menu.2ch.net/bbsmenu.html";

	// private static final String regex = "<BR><BR><B>(.*?)</B><BR>";
	// private static final Pattern categoryTitle = Pattern.compile(regex);
	// private static final Pattern boardTitle = Pattern.compile(regex);
	private static final String regex2 = ".*?(<a href=\".*?\" target=\"_blank\">&gt;&gt;([0-9]*?)</a>).*?";
	private static final Pattern REG_ANKER = Pattern.compile(regex2);

	static String s = "";
	static String location = "";

	static {
		System.setProperty("http.proxyHost", "localhost");
		System.setProperty("http.proxyPort", "8888");
	}
	private static String[][][] categoryListCache = null;

	public static final void clearCategoryListCache() {
		categoryListCache = null;
	}

	public static final String[][][] getCategoryList() {
		if (categoryListCache != null) {
			return categoryListCache;
		}

		final StringBuilder res = new StringBuilder();
		try {
			sendRequest(new URL(bbsmenu), "", new ReadLineD() {

				@Override
				public void readLine(String line) {
					res.append(line);
				}
			});
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String[] t = res.toString().split("<BR><BR><B>");
		categoryListCache = new String[t.length - 1][0][0];

		for (int i = 0, l = t.length - 1; i < l; i++) {
			String a = t[i + 1];
			String[] bs = a.split("</B><BR>");
			String[] cs = bs[1].split("<A HREF=");
			categoryListCache[i] = new String[cs.length][2];
			categoryListCache[i][0][0] = bs[0];

			for (int j = 1, m = cs.length; j < m; j++) {
				String c = cs[j];
				// if (i == 18)
				// System.out.println(j);
				String[] ds = c.split(">");
				// System.out.println(c);

				categoryListCache[i][j][0] = ds[1].replace("</A", "");
				categoryListCache[i][j][1] = ds[0].replace("TARGET=_blank", "");

			}
		}
		return categoryListCache;
	}

	public static final String[][] getThreadList(final String[] l) {
		final List<String[]> ul = new ArrayList<String[]>();
		try {
			sendRequest(new URL(l[1] + "subject.txt"), "", new ReadLineD() {

				@Override
				public void readLine(String line) {
					String[] a = line.split("<>");
					StringBuilder sb = new StringBuilder();
					sb.append(l[1]).append("dat/").append(a[0]);
					ul.add(new String[] { a[1], sb.toString() });
				}
			});
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String[][] ans = new String[ul.size()][2];
		for (int i = 0, s = ul.size(); i < s; i++) {
			ans[i] = ul.get(i);
		}
		return ans;
	}

	public static final void cacheReflesh(final int category, final int board,
			final String[] strUrl) throws IOException {
		try {
			URL url = null;
			url = new URL(strUrl[1]);
			String dat = url.getFile();
			dat = dat.substring(dat.lastIndexOf("/"));

			getContents(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public static final Res[] getResesReflesh(final int category,
			final int board, final String[] strUrl) throws IOException {
		URL url = null;
		try {
			url = new URL(strUrl[1]);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		String dat = url.getFile();
		dat = dat.substring(dat.lastIndexOf("/"));
		return getContents(url);
	}

	public static final Res[] getReses(final int category, final int board,
			final String[] strUrl) throws IOException {

		List<Res> reslist = new ArrayList<Res>();
		try {
			URL url = null;
			try {
				url = new URL(strUrl[1]);
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			}
			String dat = url.getFile();
			dat = dat.substring(dat.lastIndexOf("/"));
			return getContents(url);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return (Res[]) reslist.toArray(new Res[0]);
	}

	public static final String[] getThread(final String strUrl)
			throws IOException {

		final String[] reses = new String[1002];

		try {
			URL url = null;
			try {
				url = new URL(strUrl);
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			}
			String dat = url.getFile();
			dat = dat.substring(dat.lastIndexOf("/"));

			Res[] rowReses = getContents(url);

			final Num count = new Num();

			for (Res line : rowReses) {
				final StringBuffer bo = new StringBuffer();

				bo.append("<div>").append(line.id).append(" ")
						.append(line.name).append("<br />")
						.append(line.contents).append("</div>");
				reses[count.count] = bo.toString();
				count.incl();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return reses;
	}

	public static final String getThreadDat(final String strUrl)
			throws IOException {
		final HttpURLConnection conn;

		conn = (HttpURLConnection) new URL(strUrl).openConnection();

		InputStreamReader in = new InputStreamReader(conn.getInputStream(),
				"Shift_JIS");
		BufferedReader br = new BufferedReader(in);
		int responseCode = conn.getResponseCode();
		StringBuilder sb = new StringBuilder(1024);
		if (200 == responseCode || responseCode == 206) {
			String ss = null;
			while ((ss = br.readLine()) != null) {
				sb.append(ss).append("\n");
			}
		}
		br.close();
		
		return sb.toString();
	}

	private static final Res[] getContents(final URL url) throws IOException {
		List<Res> reslist = new ArrayList<Res>();
		final HttpURLConnection conn;
		{
			conn = (HttpURLConnection) url.openConnection();
		}

		InputStreamReader in = new InputStreamReader(conn.getInputStream());
		BufferedReader br = new BufferedReader(in);
		int responseCode = conn.getResponseCode();
		if (200 == responseCode || responseCode == 206) {
			String ss = null;
			while ((ss = br.readLine()) != null) {
				if (!"".equals(ss)) {
					String[] r = ss.split("<>");
					reslist.add(new Res(r[2], r[0], r[3]));
				}
			}
		}
		br.close();
		return (Res[]) reslist.toArray(new Res[0]);

	}

	private static final String[] sendRequest(URL url, String params,
			ReadLineD d) throws IOException {
		final String[] response = new String[3];

		final HttpURLConnection conn;
		if (url.getProtocol().equals("https")) {
			conn = (HttpsURLConnection) url.openConnection();
		} else {
			conn = (HttpURLConnection) url.openConnection();
		}
		conn.setRequestMethod("GET");
		conn.setDoOutput(true);
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		PrintWriter palWriter = new PrintWriter(byteStream);
		palWriter.print(params);
		palWriter.flush();
		if (s != null) {
			conn.setRequestProperty("Cookie", s);
		}
		byteStream.writeTo(conn.getOutputStream());
		palWriter.close();
		String c = conn.getHeaderField("Set-Cookie");
		if (c != null)
			s += ";" + c;
		response[0] = conn.getHeaderField("Last-Modified");
		response[1] = conn.getHeaderField("ETag");

		InputStreamReader isr = new InputStreamReader(conn.getInputStream(),
				"Shift_JIS");

		BufferedReader br = new BufferedReader(isr);
		String ss = br.readLine();
		while (ss != null) {
			d.readLine(ss);
			ss = br.readLine();
		}
		conn.disconnect();

		return response;
	}

	public static void main(String[] a) {
		// List<String> l = new ArrayList<String>();
		// l.add(1);
		// String s = l.get(0);

		// String[][][] cl = B2.getCategoryList();
		// for (int i = 0; i < cl.length; i++) {
		// System.out.println(cl[i][0][0]);
		//
		// for (int j = 0; j < cl[i].length; j++) {
		// System.out.println(cl[i][j][0]);
		// }
		// }
		// System.out.println(cl[0][2][0]);
		// System.out.println(cl[0][2][1]);
		// String[][] thl = B2.getThreadList(cl[0][2]);
		// System.out.println(thl[1][0]);
		// System.out.println(thl[1][1]);
		// String[] th = b.getThread(thl[1]);
		// System.out.println(th[0]);
		// System.out.println(th[1]);
		// System.out.println(th[2]);
		// Category[] cs = b.categoryList();
		// ThreadList t = b.threadList(cs[6].links[1]);
		// System.out.println(t.name + "\n" + t.links[1].name + " "
		// + t.links[1].url);
	}
}
