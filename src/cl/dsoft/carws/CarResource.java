package cl.dsoft.carws;

import java.io.File;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBElement;

import org.ini4j.InvalidFileFormatException;
import org.ini4j.Wini;

import cl.dsoft.carws.server.model.CarData;

@Path("/todo")
public class CarResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	/*
	Long idRedSocial;
	Long token;
	Long idUsuario;
	String fechaModificacion;
	*/
	
	/*
	public CarResource(UriInfo uriInfo, Request request, Long idRedSocial, Long token) {
	    this.uriInfo = uriInfo;
	    this.request = request;
	    this.idRedSocial = idRedSocial;
	    this.token = token;
	    this.idUsuario = null;
	    this.fechaModificacion = null;
	}
	  
	public CarResource(UriInfo uriInfo, Request request, Long idUsuario, String fechaModificacion) {
	    this.uriInfo = uriInfo;
	    this.request = request;
	    this.idRedSocial = null;
	    this.token = null;
	    this.idUsuario = idUsuario;
	    this.fechaModificacion = fechaModificacion;
	}
	*/
	//Application integration     
	@GET
	@Path("/byIdUsuario/{idUsuario}/{fechaModificacion}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public CarData getCarNews(
			@PathParam("idUsuario") Long idUsuario,
		    @PathParam("fechaModificacion") String fechaModificacion) {
		
		CarData carData;
		java.sql.Connection conn;
		Wini ini;
		
		carData = null;
		conn = null;
		
    	try {
    		// cargo archivo de configuracion
            ini = new Wini();
        	
        	
        	ini.load(CarResource.class.getResourceAsStream("/etc/config.ini")); 
        	
			// abro conexion a la BD
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://" + ini.get("DB", "host") + ":3306/" + ini.get("DB", "database"), 
        			ini.get("DB", "user"), ini.get("DB", "password"));
			
			carData = new CarData(conn, idUsuario, fechaModificacion);
			
			if(carData==null)
			  throw new RuntimeException("Get: not found");
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFileFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
    	
    	return carData;
	}
	
	@GET
	@Path("/byIdRedSocial/{idRedSocial}/{token}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public CarData getCarNews(
			@PathParam("idRedSocial") Long idRedSocial,
		    @PathParam("token") Long token) {
		
		CarData carData;
		java.sql.Connection conn;
		Wini ini;
		
		carData = null;
		conn = null;
		
    	try {
    		// cargo archivo de configuracion
            ini = new Wini();
        	
        	
        	ini.load(CarResource.class.getResourceAsStream("/etc/config.ini")); 
        	
			// abro conexion a la BD
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://" + ini.get("DB", "host") + ":3306/" + ini.get("DB", "database"), 
        			ini.get("DB", "user"), ini.get("DB", "password"));
			
			carData = new CarData(conn, idRedSocial, token);
			
			if(carData==null)
			  throw new RuntimeException("Get: not found");
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFileFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
    	
    	return carData;
	}
	
	@PUT
	@Path("/receive")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.TEXT_PLAIN)
	public String putTodo(JAXBElement<CarData> todo) {
		
		
		CarData carData;
		java.sql.Connection conn;
		Wini ini;
		
		carData = null;
		conn = null;
		
    	try {
    		// cargo archivo de configuracion
            ini = new Wini();
        	
        	
        	ini.load(CarResource.class.getResourceAsStream("/etc/config.ini")); 
        	
			// abro conexion a la BD
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://" + ini.get("DB", "host") + ":3306/" + ini.get("DB", "database"), 
        			ini.get("DB", "user"), ini.get("DB", "password"));
			
			carData = todo.getValue();
			
			System.out.println(todo);
			
			conn.setAutoCommit(false);

			carData.save(conn);
			
			conn.commit();
			
		    //return "ok"; //putAndGetResponse(null);
			
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFileFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (conn != null) {
				
				try {
					conn.rollback();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
    	return "ok";
		
		
	}
	
	private Response putAndGetResponse(CarData todo) {
	    Response res;
	    /*
	    if(TodoDao.instance.getModel().containsKey(todo.getId())) {
	      res = Response.noContent().build();
	    } else {
	      res = Response.created(uriInfo.getAbsolutePath()).build();
	    }
	    TodoDao.instance.getModel().put(todo.getId(), todo);
	    */
	    res = Response.ok(uriInfo.getAbsolutePath()).build();
	    
	    return res;
	    
	}
	
}
