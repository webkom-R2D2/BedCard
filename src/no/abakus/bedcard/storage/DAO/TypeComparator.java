package no.abakus.bedcard.storage.DAO;

import java.util.Comparator;

import no.abakus.naut.entity.news.Type;

public class TypeComparator implements Comparator<Type> {

	public TypeComparator() {
	}

	@Override
	public int compare(Type arg0, Type arg1) {
		return arg0.getName().compareTo(arg1.getName());
	}
}
