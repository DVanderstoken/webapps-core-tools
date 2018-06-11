package nc.noumea.mairie.webapps.core.tools.services.impl;

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


import java.util.ArrayList;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import nc.noumea.mairie.webapps.core.tools.services.GenericService;
import nc.noumea.mairie.webapps.core.tools.type.ActifInactif;

public abstract class GenericServiceImpl<T> implements GenericService<T> {

	@Override
	public abstract CrudRepository getRepository();

	@Override
	public <S extends T> S save(S var1) {
		return (S) getRepository().save(var1);
	}

	@Override
	public <S extends T> Iterable<S> save(Iterable<S> var1) {
		return getRepository().save(var1);
	}

	@Override
	public T findOne(Long var1) {
		return (T) getRepository().findOne(var1);
	}

	@Override
	public boolean exists(Long var1) {
		return getRepository().exists(var1);
	}

	@Override
	public Iterable<T> findAll() {
		return getRepository().findAll();
	}

	@Override
	public Iterable<T> findAll(Iterable<Long> var1) {
		return getRepository().findAll(var1);
	}

	@Override
	public long count() {
		return getRepository().count();
	}

	@Override
	public void delete(Long var1) {
		getRepository().delete(var1);
	}

	@Override
	public void delete(T var1) {
		getRepository().delete(var1);
	}

	@Override
	public void delete(Iterable<? extends T> var1) {
		getRepository().delete(var1);
	}

	@Override
	public void deleteAll() {
		getRepository().deleteAll();
	}

	@Override
	public List<T> construitListeActifInactif(List<T> liste) {
		List<T> result = new ArrayList<>();
		Class classe = ((T) new Object()).getClass();
		if (!classe.isInstance(ActifInactif.class)) {
			return liste;
		}

		List<T> listeActif = new ArrayList<>();
		List<T> listeInactif = new ArrayList<>();
		for (T element : liste) {
			if (element instanceof ActifInactif) {
				if (((ActifInactif) element).isActif()) {
					listeActif.add(element);
				} else {
					listeInactif.add(element);
				}
			}

		}
		result.addAll(listeActif);
		result.addAll(listeInactif);

		return result;
	}
}
