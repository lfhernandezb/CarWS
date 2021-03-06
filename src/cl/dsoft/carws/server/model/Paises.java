package cl.dsoft.carws.server.model;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "Paises", propOrder = {
	    "paises"
})
public class Paises {

	@XmlElement(name = "pais")
    protected ArrayList<cl.dsoft.carws.server.db.Pais> paises;
	
	public Paises() {
		paises = null;
	}

    public Paises(java.sql.Connection conn, Long idUsuario, String fechaModificacion) {
		seek(conn, idUsuario, fechaModificacion);
	}

    public Paises(java.sql.Connection conn, Long idRedSocial, Long token) {
		seek(conn, idRedSocial, token);
	}

    public List<cl.dsoft.carws.server.db.Pais> getPaises() {
		if (paises == null) {
			paises = new ArrayList<cl.dsoft.carws.server.db.Pais>();
		}
		return paises;
	}

    private void seek(java.sql.Connection conn, Long idUsuario, String fechaModificacion) {
    	
    	ArrayList<AbstractMap.SimpleEntry<String, String>> listParameters;
    	
    	try {
			listParameters = new ArrayList<AbstractMap.SimpleEntry<String, String>>();
			
			listParameters.add(new AbstractMap.SimpleEntry<String, String>("id_usuario", String.valueOf(idUsuario)));
			listParameters.add(new AbstractMap.SimpleEntry<String, String>("mas reciente", fechaModificacion));
			
			this.paises = cl.dsoft.carws.server.db.Pais.seek(conn, listParameters, null, null, 0, 1);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }

    private void seek(java.sql.Connection conn, Long idRedSocial, Long token) {
    	
    	ArrayList<AbstractMap.SimpleEntry<String, String>> listParameters;
    	
    	try {
			listParameters = new ArrayList<AbstractMap.SimpleEntry<String, String>>();
			
			listParameters.add(new AbstractMap.SimpleEntry<String, String>("id_red_social", String.valueOf(idRedSocial)));
			listParameters.add(new AbstractMap.SimpleEntry<String, String>("token", String.valueOf(token)));
			
			this.paises = cl.dsoft.carws.server.db.Pais.seek(conn, listParameters, null, null, 0, 1);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
}
