package org.jboss.tools.hb.ui.bot.test.generation;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.jboss.tools.hb.ui.bot.common.ConfigurationFile;
import org.jboss.tools.hb.ui.bot.common.PersistenceXML;
import org.jboss.tools.hb.ui.bot.test.HibernateBaseTest;
import org.jboss.tools.ui.bot.ext.config.Annotations.DB;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.helper.ContextMenuHelper;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.junit.Test;

@Require(db = @DB, clearProjects = true, perspective = "JPA")
public class JPAEntitiesGenerationTest extends HibernateBaseTest {
	
	final String prj = "jpatest35";
	final String out = "src";
	final String hbcfg = "hibernate.cfg.xml";
	
	@Test
	public void generateJPAEntitiesFromDB() {
		importTestProject("/resources/prj/" + prj);
		createHBConfigurationAndSetPersistence();
		generateEntitiesFromDB();
	}

	private void createHBConfigurationAndSetPersistence() {
		ConfigurationFile.create(new String[]{prj,"src"}, hbcfg,false);
		PersistenceXML.openPersistenceXML(prj);
		PersistenceXML.setHibernateConfiguration("/" + hbcfg);		
	}

	private void generateEntitiesFromDB() {
		SWTBotView viewBot = bot.viewByTitle(IDELabel.View.PROJECT_EXPLORER);
		SWTBotTree tree = viewBot.bot().tree().select(prj);

		// JPA Tools -> Generate Tables From Entities
		// workaround for https://issues.jboss.org/browse/JBIDE-12796
		try {
			ContextMenuHelper.clickContextMenu(tree, "JPA Tools",
				"Generate Entities from Tables...");
		}
		catch(Exception e) {
			ContextMenuHelper.clickContextMenu(tree, "JPA Tools",
					"Generate Entities from Tables...");
		}

		// DDL Generation Dialog
		String outputDir = prj + "/" + out;
		bot.textWithLabel("Output directory:").setText(outputDir);
		bot.textWithLabel("Package:").setText("org.gen");

		bot.button(IDELabel.Button.FINISH).click();		
	}
}