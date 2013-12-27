package jp.blogspot.simple_asta.b2.client;

import java.io.UnsupportedEncodingException;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ThreadClickHandler implements ClickHandler {
	@SuppressWarnings("unused")
	private static final String regex2 = ".*?(<a href=\".*?\" target=\"_blank\">&gt;&gt;([0-9]*?)</a>).*?";
	// private static final Pattern REG_ANKER = Pattern.compile(regex2);

	private final GreetingServiceAsync greetingService;
	private final HTML detail;

	public ThreadClickHandler(final GreetingServiceAsync greetingService_,
			final HTML detail_) {
		this.greetingService = greetingService_;
		this.detail = detail_;
	}

	@Override
	public void onClick(ClickEvent event) {
		int sel = ((ListBox) event.getSource()).getSelectedIndex();

		String url = ((ListBox) event.getSource()).getValue(sel);

		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Board");
		dialogBox.setAnimationEnabled(false);
		final Button closeButton = new Button("Close");

		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");
		final HTML serverResponseLabel = new HTML();
		final VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(serverResponseLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		serverResponseLabel.setText("");
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
			}
		});
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);
		greetingService.greetServer("Thread 1 1 " + url,
				new AsyncCallback<byte[]>() {
					public void onFailure(Throwable caught) {
						dialogBox.setText("Remote Procedure Call - Failure");
						serverResponseLabel
								.addStyleName("serverResponseLabelError");
						serverResponseLabel.setHTML(B2devel.SERVER_ERROR);
						dialogBox.center();
						closeButton.setFocus(true);
					}

					@Override
					public void onSuccess(byte[] resultb) {
						serverResponseLabel
								.removeStyleName("serverResponseLabelError");
						String result = "";
						try {
							result = new String(resultb, "UTF-8");
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
						detail.setHTML((result));
					}

					private String formatHTML(String result) {
						StringBuilder fh = new StringBuilder();
						String[] lines = result.split("\n");
						final Num count = new Num();
						for (String line : lines) {
							final StringBuffer bo = new StringBuffer();

							String[] r = line.split("<>");

							bo.append("<div id=r_").append(count)
									.append("><font size=2><b>").append(count)
									.append(" ").append(r[2])
									.append("</b></font><br>");
							/**/
							bo.append(r[3]).append("</div>");
							fh.append(bo.toString()).append("\n");
							count.incl();

						}
						return fh.toString();
					}
				});
	}

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
