package jp.simple_asta.blog.b2.client;

import java.io.UnsupportedEncodingException;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ThreadListSelectionHandler implements SelectionHandler<TreeItem> {
	private final GreetingServiceAsync greetingService;

	public ThreadListSelectionHandler(
			final GreetingServiceAsync greetingService_) {
		this.greetingService = greetingService_;

	}

	@Override
	public void onSelection(SelectionEvent<TreeItem> event) {
		TreeItem parent = event.getSelectedItem().getParentItem();
		if (parent != null) {
			final String categoryid = (String) parent.getUserObject();
			final String boardid = (String) event.getSelectedItem()
					.getUserObject();

			final DialogBox dialogBox = new DialogBox();
			dialogBox.setText("Board");
			dialogBox.setAnimationEnabled(false);
			final Button closeButton = new Button("Close");

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
			greetingService.greetServer("ThreadList " + categoryid + " "
					+ boardid, new AsyncCallback<byte[]>() {
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
					B2devel.list.clear();
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
						B2devel.list.addItem(t[1], t[2] );
					}

					B2devel.list.setVisibleItemCount(names.length);
				}
			});
			dialogVPanel.add(closeButton);
			dialogBox.setWidget(dialogVPanel);

		}
	}

}
