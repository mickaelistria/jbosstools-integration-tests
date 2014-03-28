package org.jboss.tools.openshift.ui.bot.test.app;

import java.util.Iterator;

import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.ButtonWithTextIsActive;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.openshift.ui.bot.test.OpenShiftBotTest;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftExplorerView;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;
import org.junit.After;
import org.junit.Test;

public class CreateApplicationFromGithub {

	private static final String APP_NAME = "wordpressapp" + System.currentTimeMillis();
	private static final String URL = "https://github.com/openshift/wordpress-example";
	
	@Test
	public void createAppFromGithubTemplate() {
		createAppFromGithub(OpenShiftLabel.Cartridge.MYSQL_ONLINE);
	}
	
	public static void createAppFromGithub(String mySQLLabel) {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.open();
		
		TreeItem connection = new DefaultTree().getItems().get(0);
		connection.select();
		new ContextMenu("New", "Application...").select();
		
		new WaitUntil(new ShellWithTextIsAvailable("New OpenShift Application"), TimePeriod.LONG);
		
		new DefaultShell("New OpenShift Application").setFocus();
		if (!(new RadioButton(1).isSelected())) {
			new RadioButton(1).click();
		}
		
		Iterator<TreeItem> iterator = new DefaultTree().getAllItems().iterator();
		while(iterator.hasNext()) {
			TreeItem cartridgeItem = iterator.next();
			if (cartridgeItem.getText().equals(OpenShiftLabel.AppType.PHP)) {
				cartridgeItem.select();
				break;
			}
		}

		new WaitUntil(new ButtonWithTextIsActive(new PushButton(
				OpenShiftLabel.Button.NEXT)), TimePeriod.LONG);
		
		new PushButton(OpenShiftLabel.Button.NEXT).click();
		
		// bcs there is no running job it is required to verify this way
		new WaitUntil(new ButtonWithTextIsActive(new PushButton(
				OpenShiftLabel.Button.BACK)), TimePeriod.LONG);
		
		new LabeledText("Name:").setText(APP_NAME);
		
		new PushButton("Add...").click();
		
		new WaitUntil(new ShellWithTextIsAvailable("Add Embedded Cartridges"),
				TimePeriod.NORMAL);
		
		new DefaultShell("Add Embedded Cartridges").setFocus();
		new DefaultTable().getItem(mySQLLabel).setChecked(true);
		new PushButton("OK").click();
		
		new DefaultShell("New OpenShift Application").setFocus();
		
		new PushButton(OpenShiftLabel.Button.ADVANCED).click();
		new CheckBox(1).click();
		new LabeledText("Source code:").setText(URL);
		
		new WaitUntil(new ButtonWithTextIsActive(new PushButton(OpenShiftLabel.Button.NEXT)), TimePeriod.NORMAL);
		
		new PushButton(OpenShiftLabel.Button.NEXT).click();
		new PushButton(OpenShiftLabel.Button.NEXT).click();
		
		
		new WaitUntil(new ButtonWithTextIsActive(new PushButton(OpenShiftLabel.Button.FINISH)), TimePeriod.NORMAL);
		
		new PushButton(OpenShiftLabel.Button.FINISH).click();
		
		new WaitUntil(new ShellWithTextIsAvailable("Embedded Cartridges"), TimePeriod.VERY_LONG);
		
		new DefaultShell("Embedded Cartridges").setFocus();
		new PushButton(OpenShiftLabel.Button.OK).click();
		
		new WaitUntil(new ShellWithTextIsAvailable("Question"), TimePeriod.VERY_LONG);
		
		DefaultShell question = new DefaultShell("Question");
		question.setFocus();
		new PushButton(OpenShiftLabel.Button.YES).click();
		
		new WaitUntil(new ShellWithTextIsAvailable("Publish " + APP_NAME + "?"), TimePeriod.VERY_LONG);
		
		new DefaultShell("Publish " + APP_NAME + "?").setFocus();
		new PushButton(OpenShiftLabel.Button.YES).click();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
		
		// TODO verify in Browser
	}

	@After
	public void deleteApplication() {
		deleteApp();
	}
	
	public static void deleteApp() {
		OpenShiftBotTest.deleteOpenShiftApplication(APP_NAME, OpenShiftLabel.AppType.PHP_TREE);
	}
}
