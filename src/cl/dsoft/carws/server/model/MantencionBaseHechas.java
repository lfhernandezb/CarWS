package cl.dsoft.carws.server.model;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "MantencionBaseHechas", propOrder = {
	    "mantencionBaseHechas"
})
public class MantencionBaseHechas {

	@XmlElement(name = "mantencionBaseHecha")
    protected ArrayList<cl.dsoft.carws.server.db.MantencionBaseHecha> mantencionBaseHechas;

    public MantencionBaseHechas() {
		mantencionBaseHechas = null;
	}

    public MantencionBaseHechas(java.sql.Connection conn, Long idBase, String fechaModificacion) {
		seek(conn, idBase, fechaModificacion);
	}

    public List<cl.dsoft.carws.server.db.MantencionBaseHecha> getMantencionBaseHechas() {
		if (mantencionBaseHechas == null) {
			mantencionBaseHechas = new ArrayList<cl.dsoft.carws.server.db.MantencionBaseHecha>();
		}
		return mantencionBaseHechas;
	}

    private void seek(java.sql.Connection conn, Long idBase, String fechaModificacion) {
    	
    	ArrayList<AbstractMap.SimpleEntry<String, String>> listParameters;
    	
    	try {
			listParameters = new ArrayList<AbstractMap.SimpleEntry<String, String>>();
			
			listParameters.add(new AbstractMap.SimpleEntry<String, String>("id_usuario", String.valueOf(idBase)));
			listParameters.add(new AbstractMap.SimpleEntry<String, String>("mas reciente", fechaModificacion));
			
			this.mantencionBaseHechas = cl.dsoft.carws.server.db.MantencionBaseHecha.seek(conn, listParameters, null, null, 0, 1);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
}
