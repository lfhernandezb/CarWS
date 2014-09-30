package cl.dsoft.carws.server.model;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "Autenticaciones", propOrder = {
	    "autenticaciones"
})
public class Autenticaciones {

	@XmlElement(name = "autenticacion")
    protected ArrayList<cl.dsoft.carws.server.db.Autenticacion> autenticaciones;
	
	public Autenticaciones() {
		autenticaciones = null;
	}

    public Autenticaciones(java.sql.Connection conn, Long idUsuario, String fechaModificacion) {
		seek(conn, idUsuario, fechaModificacion);
	}

    public Autenticaciones(java.sql.Connection conn, Long idRedSocial, Long token) {
		seek(conn, idRedSocial, token);
	}

    public List<cl.dsoft.carws.server.db.Autenticacion> getAutenticaciones() {
		if (autenticaciones == null) {
			autenticaciones = new ArrayList<cl.dsoft.carws.server.db.Autenticacion>();
		}
		return autenticaciones;
	}

    private void seek(java.sql.Connection conn, Long idUsuario, String fechaModificacion) {
    	
    	ArrayList<AbstractMap.SimpleEntry<String, String>> listParameters;
    	
    	try {
			listParameters = new ArrayList<AbstractMap.SimpleEntry<String, String>>();
			
			listParameters.add(new AbstractMap.SimpleEntry<String, String>("id_usuario", String.valueOf(idUsuario)));
			listParameters.add(new AbstractMap.SimpleEntry<String, String>("mas reciente", fechaModificacion));
			
			this.autenticaciones = cl.dsoft.carws.server.db.Autenticacion.seek(conn, listParameters, null, null, 0, 1);
			
			
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
			
			this.autenticaciones = cl.dsoft.carws.server.db.Autenticacion.seek(conn, listParameters, null, null, 0, 1);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
}
