package cl.dsoft.carws.server.model;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "Regiones", propOrder = {
	    "regiones"
})
public class Regiones {

	@XmlElement(name = "region")
    protected ArrayList<cl.dsoft.carws.server.db.Region> regiones;
	
	public Regiones() {
		regiones = null;
	}

    public Regiones(java.sql.Connection conn, Long idUsuario, String fechaModificacion) {
		seek(conn, idUsuario, fechaModificacion);
	}

    public Regiones(java.sql.Connection conn, Long idRedSocial, Long token) {
		seek(conn, idRedSocial, token);
	}

    public List<cl.dsoft.carws.server.db.Region> getRegiones() {
		if (regiones == null) {
			regiones = new ArrayList<cl.dsoft.carws.server.db.Region>();
		}
		return regiones;
	}

    private void seek(java.sql.Connection conn, Long idUsuario, String fechaModificacion) {
    	
    	ArrayList<AbstractMap.SimpleEntry<String, String>> listParameters;
    	
    	try {
			listParameters = new ArrayList<AbstractMap.SimpleEntry<String, String>>();
			
			listParameters.add(new AbstractMap.SimpleEntry<String, String>("id_usuario", String.valueOf(idUsuario)));
			listParameters.add(new AbstractMap.SimpleEntry<String, String>("mas reciente", fechaModificacion));
			
			this.regiones = cl.dsoft.carws.server.db.Region.seek(conn, listParameters, null, null, 0, 1);
			
			
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
			
			this.regiones = cl.dsoft.carws.server.db.Region.seek(conn, listParameters, null, null, 0, 1);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
}
