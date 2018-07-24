package nc.noumea.mairie.webapps.core.tools.zk.viewmodel;

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

import nc.noumea.mairie.webapps.core.tools.domain.AbstractEntity;
import nc.noumea.mairie.webapps.core.tools.zk.event.AfterSaveAbstractEntityEvent;
import nc.noumea.mairie.webapps.core.tools.zk.event.BeforeSaveAbstractEntityEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;

import javax.persistence.OptimisticLockException;

/**
 * ViewModel abstrait parent des ViewModel de modification (qui permettent de modifier une entité dans un onglet).
 *
 * @param <T> Type paramétré (représente une classe d'entité en pratique)
 * @author AgileSoft.NC
 */
public abstract class AbstractPopupEditViewModel<T extends AbstractEntity> extends AbstractEditViewModel<T> {

	private static Logger log = LoggerFactory.getLogger(AbstractPopupEditViewModel.class);

	@Init
	public void initSetup(@ExecutionArgParam("entity") T abstractEntity) {
		super.initSetup(abstractEntity);
	}

	/**
	 * Met à jour en base de données l'entité (typiquement sur clic d'un bouton enregistrer), et notifie par une commande globale la mise à jour de l'entité. Si
	 * l'entité présente des contraintes de violation, une popup apparait et la sauvegarde en base n'a pas lieu.
	 */
	@Command
	public void update(@ContextParam(ContextType.VIEW) Component view) {
		if (showErrorPopup(this.entity)) {
			return;
		}

		BeforeSaveAbstractEntityEvent eventBeforeSave = new BeforeSaveAbstractEntityEvent(entity, view);
		Events.sendEvent(eventBeforeSave);

		if (eventBeforeSave.isStopSave()) {
			return;
		}

		try {
			this.entity = getService().save(this.entity);
			super.showNotificationEntityEnregistre();
			notifyUpdateEntity();
			view.detach();
			Events.sendEvent(new AfterSaveAbstractEntityEvent(this.entity, view));
		} catch (OptimisticLockException e) {
			showErrorPopup("Sauvegarde impossible, l'enregistrement a été modifié par un autre utilisateur (veuillez recharger et resaisir vos modifications)");
			log.warn("Modification concurrente (l'utilisateur a été averti avec un message compréhensible)", e);
		}
	}


}
