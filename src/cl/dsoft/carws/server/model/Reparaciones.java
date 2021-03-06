package cl.dsoft.carws.server.model;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "Reparaciones", propOrder = {
	    "reparaciones"
})
public class Reparaciones {

	@XmlElement(name = "reparacion")
    protected ArrayList<cl.dsoft.carws.server.db.Reparacion> reparaciones;

    public Reparaciones() {
		reparaciones = null;
	}

    public Reparaciones(java.sql.Connection conn, Long idUsuario, String fechaModificacion) {
		seek(conn, idUsuario, fechaModificacion);
	}

    public List<cl.dsoft.carws.server.db.Reparacion> getReparaciones() {
		if (reparaciones == null) {
			reparaciones = new ArrayList<cl.dsoft.carws.server.db.Reparacion>();
		}
		return reparaciones;
	}

    private void seek(java.sql.Connection conn, Long idUsuario, String fechaModificacion) {
    	
    	ArrayList<AbstractMap.SimpleEntry<String, String>> listParameters;
    	
    	try {
			listParameters = new ArrayList<AbstractMap.SimpleEntry<String, String>>();
			
			listParameters.add(new AbstractMap.SimpleEntry<String, String>("id_usuario", String.valueOf(idUsuario)));
			listParameters.add(new AbstractMap.SimpleEntry<String, String>("mas reciente", fechaModificacion));
			
			this.reparaciones = cl.dsoft.carws.server.db.Reparacion.seek(conn, listParameters, null, null, 0, 1);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
}
