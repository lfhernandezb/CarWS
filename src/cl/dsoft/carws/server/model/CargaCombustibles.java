package cl.dsoft.carws.server.model;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "CargaCombustibles", propOrder = {
	    "cargaCombustibles"
})
public class CargaCombustibles {

	@XmlElement(name = "cargaCombustible")
    protected ArrayList<cl.dsoft.carws.server.db.CargaCombustible> cargaCombustibles;

    public CargaCombustibles() {
    	cargaCombustibles = null;
	}

    public CargaCombustibles(java.sql.Connection conn, Long idUsuario, String fechaModificacion) {
		seek(conn, idUsuario, fechaModificacion);
	}

    public List<cl.dsoft.carws.server.db.CargaCombustible> getCargaCombustibles() {
		if (cargaCombustibles == null) {
			cargaCombustibles = new ArrayList<cl.dsoft.carws.server.db.CargaCombustible>();
		}
		return cargaCombustibles;
	}

    private void seek(java.sql.Connection conn, Long idUsuario, String fechaModificacion) {
    	
    	ArrayList<AbstractMap.SimpleEntry<String, String>> listParameters;
    	
    	try {
			listParameters = new ArrayList<AbstractMap.SimpleEntry<String, String>>();
			
			listParameters.add(new AbstractMap.SimpleEntry<String, String>("id_usuario", String.valueOf(idUsuario)));
			listParameters.add(new AbstractMap.SimpleEntry<String, String>("mas reciente", "'" + fechaModificacion + "'"));
			
			this.cargaCombustibles = cl.dsoft.carws.server.db.CargaCombustible.seek(conn, listParameters, null, null, 0, 1);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
}
