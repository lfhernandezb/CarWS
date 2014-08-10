package cl.dsoft.carws.server.model;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "MantencionUsuario", propOrder = {
	    "mantencionUsuario"
})
public class MantencionUsuario {

	@XmlElement(name = "mantencionUsuario")
    protected ArrayList<cl.dsoft.carws.server.db.MantencionUsuario> mantencionUsuario;

    public MantencionUsuario() {
		mantencionUsuario = null;
	}

    public MantencionUsuario(java.sql.Connection conn, Long idUsuario, String fechaModificacion) {
		seek(conn, idUsuario, fechaModificacion);
	}

    public List<cl.dsoft.carws.server.db.MantencionUsuario> getMantencionUsuario() {
		if (mantencionUsuario == null) {
			mantencionUsuario = new ArrayList<cl.dsoft.carws.server.db.MantencionUsuario>();
		}
		return mantencionUsuario;
	}

    private void seek(java.sql.Connection conn, Long idUsuario, String fechaModificacion) {
    	
    	ArrayList<AbstractMap.SimpleEntry<String, String>> listParameters;
    	
    	try {
			listParameters = new ArrayList<AbstractMap.SimpleEntry<String, String>>();
			
			listParameters.add(new AbstractMap.SimpleEntry<String, String>("id_usuario", String.valueOf(idUsuario)));
			listParameters.add(new AbstractMap.SimpleEntry<String, String>("mas reciente", fechaModificacion));
			
			this.mantencionUsuario = cl.dsoft.carws.server.db.MantencionUsuario.seek(conn, listParameters, null, null, 0, 1);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
}
