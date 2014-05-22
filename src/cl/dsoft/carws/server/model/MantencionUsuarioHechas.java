package cl.dsoft.carws.server.model;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "MantencionUsuarioHechas", propOrder = {
	    "mantencionUsuarioHechas"
})
public class MantencionUsuarioHechas {

	@XmlElement(name = "mantencionUsuarioHecha")
    protected ArrayList<cl.dsoft.carws.server.db.MantencionUsuarioHecha> mantencionUsuarioHechas;

    public MantencionUsuarioHechas() {
		mantencionUsuarioHechas = null;
	}

    public MantencionUsuarioHechas(java.sql.Connection conn, Long idUsuario, String fechaModificacion) {
		seek(conn, idUsuario, fechaModificacion);
	}

    public List<cl.dsoft.carws.server.db.MantencionUsuarioHecha> getMantencionUsuarioHechas() {
		if (mantencionUsuarioHechas == null) {
			mantencionUsuarioHechas = new ArrayList<cl.dsoft.carws.server.db.MantencionUsuarioHecha>();
		}
		return mantencionUsuarioHechas;
	}

    private void seek(java.sql.Connection conn, Long idUsuario, String fechaModificacion) {
    	
    	ArrayList<AbstractMap.SimpleEntry<String, String>> listParameters;
    	
    	try {
			listParameters = new ArrayList<AbstractMap.SimpleEntry<String, String>>();
			
			listParameters.add(new AbstractMap.SimpleEntry<String, String>("id_usuario", String.valueOf(idUsuario)));
			listParameters.add(new AbstractMap.SimpleEntry<String, String>("mas reciente", "'" + fechaModificacion + "'"));
			
			this.mantencionUsuarioHechas = cl.dsoft.carws.server.db.MantencionUsuarioHecha.seek(conn, listParameters, null, null, 0, 1);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
}
