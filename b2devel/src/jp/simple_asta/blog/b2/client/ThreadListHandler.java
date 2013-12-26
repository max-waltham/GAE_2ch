package jp.simple_asta.blog.b2.client;

import java.io.UnsupportedEncodingException;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ThreadListHandler implements ClickHandler {
	private final GreetingServiceAsync greetingService;
	private final String categoryid;
	private final String boardid;

	public ThreadListHandler(final GreetingServiceAsync greetingService_,
			final String categoryid_, final String boardid_) {
		this.greetingService = greetingService_;
		this.categoryid = categoryid_;
		this.boardid = boardid_;
	}

	@Override
	public void onClick(ClickEvent event) {
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
		greetingService.greetServer("ThreadList " + categoryid + " " + boardid,
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
						dialogBox.setText("ThreadList");
						serverResponseLabel
								.removeStyleName("serverResponseLabelError");
						String result = "";
						try {
							result = new String(resultb, "UTF-8");
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
						String[] names = result.split("><");
						for (String name : names) {
							String[] t = name.split("<>");
							//final String tid = t[0];
							Button tButton = new Button(t[1]);
							 tButton.addClickHandler(new ThreadHandler(
							 greetingService, categoryid,boardid,t[2]));
							dialogVPanel.add(tButton);
						}
						dialogBox.show();
						closeButton.setFocus(true);
						
					}
				});
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);

	}

}
