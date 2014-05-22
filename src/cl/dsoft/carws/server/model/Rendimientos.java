package cl.dsoft.carws.server.model;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "Rendimientos", propOrder = {
	    "rendimientos"
})
public class Rendimientos {

	@XmlElement(name = "rendimiento")
    protected ArrayList<cl.dsoft.carws.server.db.Rendimiento> rendimientos;

    public Rendimientos() {
		rendimientos = null;
	}

    public Rendimientos(java.sql.Connection conn, Long idUsuario, String fechaModificacion) {
		seek(conn, idUsuario, fechaModificacion);
	}

    public List<cl.dsoft.carws.server.db.Rendimiento> getRendimientos() {
		if (rendimientos == null) {
			rendimientos = new ArrayList<cl.dsoft.carws.server.db.Rendimiento>();
		}
		return rendimientos;
	}

    private void seek(java.sql.Connection conn, Long idUsuario, String fechaModificacion) {
    	
    	ArrayList<AbstractMap.SimpleEntry<String, String>> listParameters;
    	
    	try {
			listParameters = new ArrayList<AbstractMap.SimpleEntry<String, String>>();
			
			listParameters.add(new AbstractMap.SimpleEntry<String, String>("id_usuario", String.valueOf(idUsuario)));
			listParameters.add(new AbstractMap.SimpleEntry<String, String>("mas reciente", "'" + fechaModificacion + "'"));
			
			this.rendimientos = cl.dsoft.carws.server.db.Rendimiento.seek(conn, listParameters, null, null, 0, 1);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
}
