package nc.noumea.mairie.webapps.core.tools.docx;

/*-
 * #%L
 * WebApps Core Tools
 * %%
 * Copyright (C) 2018 Mairie de Nouméa, Nouvelle-Calédonie
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

/**
 * Test de génération de docx
 */

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class TemplateDocxTest {

	private static Logger log = LoggerFactory.getLogger(TemplateDocxTest.class);

	public static final String DOCX_BASE_DIR = "./src/test/java/nc/noumea/mairie/webapps/core/tools/docx/";

	@Test
	public void testCreateDocx() throws IOException, Docx4JException, JAXBException {

		TemplateDocx templateDocx = new TemplateDocx(new File(DOCX_BASE_DIR + "demo-template.docx"));

		// Subsitution de texte simple
		templateDocx.setText("nomDeFamille", "Dupond");

		// Gestion des checkboxs
		templateDocx.setCheckBox("estUnHomme", true);
		templateDocx.setCheckBox("estUneFemme", false);

		// Insertion d'une image
		File imageExemple = new File(DOCX_BASE_DIR + "imageExemple.png");
		byte[] imageExempleByteArray = FileUtils.readFileToByteArray(imageExemple);
		templateDocx.setText("monImage", new String(Base64.encodeBase64(imageExempleByteArray)));

		// Remplissage d'un tableau dynamique et suppression de l'entête
		TableFilling tableFilling = new TableFilling("1ere ligne de tableau");
		// Suppression de la ligne d'entête
		tableFilling.setRemoveTitleRow(true);

		// remplir les trois premières cases de la première ligne
		Map<String, String> mapCodeValeurLigne1 = new HashMap<>();
		mapCodeValeurLigne1.put("1", "Ligne 1 - Colonne 1");
		mapCodeValeurLigne1.put("2", "Ligne 1 - Colonne 2");
		mapCodeValeurLigne1.put("3", "Ligne 1 - Colonne 3");
		tableFilling.getListeMapCodeValeur().add(mapCodeValeurLigne1);
		//
		Map<String, String> mapCodeValeurLigne2 = new HashMap<>();
		mapCodeValeurLigne2.put("1", "Ligne 2 - Colonne 1");
		mapCodeValeurLigne2.put("2", "Ligne 2 - Colonne 2");
		mapCodeValeurLigne2.put("3", "Ligne 2 - Colonne 3");
		tableFilling.getListeMapCodeValeur().add(mapCodeValeurLigne2);

		// Ajout de la table au template
		templateDocx.addTableFilling(tableFilling);

		// Création du document de résultat
		File fichierResultat = File.createTempFile("demo-resultat", ".docx");
		log.info("fichier généré = " + fichierResultat.getAbsolutePath());
		templateDocx.createDocx(fichierResultat);
		assertTrue(fichierResultat.exists());
		assertTrue(fichierResultat.length() > 30000d);
	}

	@Test
	public void testAddParagraphOfTextNotUsingTemplateDocxWrapper() throws Docx4JException, IOException {
		WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
		wordMLPackage.getMainDocumentPart().addParagraphOfText("Ouh yeah!");
		File fichierResultat = File.createTempFile("demo-resultat2", ".docx");
		wordMLPackage.save(fichierResultat);
		assertTrue(fichierResultat.exists());
		assertTrue(fichierResultat.length() > 5000d);
	}
}
