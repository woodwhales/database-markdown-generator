package org.woodwhales.model.md;

import org.woodwhales.model.db.Tabel;

public interface MDTableInfoExcutor {

	String genHeader();

	String genHeaderLine();

	String genBody(Tabel tabelInfo);

}
