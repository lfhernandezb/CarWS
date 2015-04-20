package cl.dsoft.carws2;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.sql.DataSource;
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
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.Wini;
import org.joda.time.DateTime;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.codec.binary.Base64;

import cl.dsoft.car.server.db.Autenticacion;
import cl.dsoft.car.server.db.Comuna;
import cl.dsoft.car.server.db.ConsultaProveedor;
import cl.dsoft.car.server.db.InfoSincro;
import cl.dsoft.car.server.db.Proveedor;
import cl.dsoft.car.server.db.RespuestaProveedor;
import cl.dsoft.car.server.db.Usuario;
import cl.dsoft.car.server.model.CarData;
import cl.dsoft.car.server.model.RespuestaProveedorData;
import cl.dsoft.car.server.model.ImageData;
import cl.dsoft.car.server.model.Usuarios;
import cl.dsoft.car.server.model.Vigencia;

@Path("/")
public class CarResource {
	/**
	 * 
	 */

	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
	
	@javax.ws.rs.core.Context 
	ServletContext context;	
	
	DataSource ds;
	
	static Logger log = Logger.getLogger(CarResource.class);
	
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
	public CarResource() throws ServletException {
		
	    try {
	    	/*
	    	 //Create a datasource for pooled connections.
	    	 datasource = (DataSource) context.getAttribute("DBCPool");
			*/
	    	//Register the driver for non-pooled connections.
	    	//Class.forName("com.mysql.jdbc.Driver").newInstance();
	    }
	    catch (Exception e) {
	        throw new ServletException(e.getMessage());
	    }	
	}

	//Application integration     
	@GET
	@Path("/byIdUsuario/{idUsuario}/{fechaModificacion}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public CarData getCarNewsByIdUsuario(
			@PathParam("idUsuario") Long idUsuario,
		    @PathParam("fechaModificacion") String fechaModificacion) {
		
		CarData carData;
		java.sql.Connection conn;
		
		carData = null;
		conn = null;
		
    	try {
    		log.info("byIdUsuario input: " + String.valueOf(idUsuario) + "/" + fechaModificacion);
    		conn = getConnection(true);
			
			carData = new CarData(conn, idUsuario, fechaModificacion);
			
			if (!carData.getUsuarios().getUsuarios().isEmpty()) {
				
				InfoSincro is = new InfoSincro();
				
				is.setSentido((byte) InfoSincro.tipoSincro.SERVER_TO_PHONE.getCode());
				is.setUsuarioIdUsuario(idUsuario);
				is.setFecha(new Date());
				
				is.insert(conn);
			}
			
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			conn = null;
			
			log.info("byIdUsuario output: " + carData.toString());
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFileFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NamingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
	public CarData getCarNewsByIdRedSocial(
			@PathParam("idRedSocial") Long idRedSocial,
		    @PathParam("token") String token) {
		
		CarData carData;
		java.sql.Connection conn;
		
		carData = null;
		conn = null;
		
    	try {
    		log.info("byIdRedSocial input: " + String.valueOf(idRedSocial) + "/" + token);
    		conn = getConnection(true);
			
			carData = new CarData(conn, idRedSocial, token, true);
			
			if (!carData.getUsuarios().getUsuarios().isEmpty()) {
			
				InfoSincro is = new InfoSincro();
				
				is.setSentido((byte) InfoSincro.tipoSincro.SERVER_TO_PHONE.getCode());
				is.setUsuarioIdUsuario(carData.getUsuarios().getUsuarios().get(0).getId());
				is.setFecha(new Date());
				
				is.insert(conn);
			}
			
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			conn = null;

			//log.info("byIdRedSocial output: " + carData.toString());

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
		} catch (NamingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
	
	// crea un nuevo usuario utilizando las credenciales de red social e idComuna
	
	@GET
	@Path("/createUser/{idRedSocial}/{token}/{idComuna}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public CarData getNewUser(
			@PathParam("idRedSocial") Long idRedSocial,
		    @PathParam("token") String token,
		    @PathParam("idComuna") Long idComuna) {
		
		CarData carData;
		java.sql.Connection conn;
		
		carData = null;
		conn = null;
		
    	try {
    		log.info("createUser input: " + String.valueOf(idRedSocial) + "/" + token);
    		conn = getConnection(true);
			
			// chequeos
			
			// quiza usuario existe....
			
			Usuario u = null;
			
			Usuarios us = new Usuarios(conn, idRedSocial, token, true);
			
			if (us.getUsuarios().isEmpty()) {
				
				conn.setAutoCommit(false);
				
				conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
				
				u = new Usuario();
				
				u.setIdComuna(idComuna);
				
				u.insert(conn);
				
				Autenticacion a = new Autenticacion();
				
				a.setIdRedSocial(idRedSocial);
				a.setToken(token);
				a.setIdUsuario(u.getId());
				
				a.insert(conn);
								
				carData = new CarData(conn, idRedSocial, token, true);
				/*
				InfoSincro is = new InfoSincro();
				
				is.setSentido((byte) InfoSincro.tipoSincro.SERVER_TO_PHONE.getCode());
				is.setUsuarioIdUsuario(u.getId());
				is.setFecha(new Date());
				
				is.insert(conn);
				*/
				conn.commit();

			}			
			
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			conn = null;
			
			//log.info("getProveedores output: " + carData.toString());

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
		} catch (NamingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
    	
    	return carData;
	}

	@GET
	@Path("/getProveedores/{idUsuario}/{idVehiculo}/{idMantencionBase}/{latitud}/{longitud}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public RespuestaProveedorData getProveedores(
			@PathParam("idUsuario") Long idUsuario,
		    @PathParam("idVehiculo") Long idVehiculo,
		    @PathParam("idMantencionBase") Long idMantencionBase,
		    @PathParam("latitud") Double latitud,
		    @PathParam("longitud") Double longitud) {
		
		RespuestaProveedorData rpData;
		java.sql.Connection conn;
		ArrayList<AbstractMap.SimpleEntry<String, String>> listParameters;
		
		rpData = null;
		conn = null;
		
    	try {
    		log.info("getProveedores input: " + String.valueOf(idUsuario) + "/" + String.valueOf(idVehiculo)+ "/" + String.valueOf(idMantencionBase)+ "/" + String.valueOf(latitud)+ "/" + String.valueOf(longitud));
    		
    		RespuestaProveedor rp;
    		
    		conn = getConnection(true);
    		
    		conn.setAutoCommit(false);

    		ConsultaProveedor cp = new ConsultaProveedor();
    		
    		cp.setIdUsuario(idUsuario);
    		cp.setIdVehiculo(idVehiculo);
    		cp.setIdMantencionBase(idMantencionBase);
    		cp.setLatitud(latitud);
    		cp.setLongitud(longitud);
    		
    		cp.insert(conn);
    		    		
    		rpData = new RespuestaProveedorData(conn, cp);
    		
    		for (Proveedor p : rpData.getProveedores().getProveedores()) {
    			rp = new RespuestaProveedor();
    			
    			rp.setIdVehiculo(idVehiculo);
    			rp.setIdUsuario(idUsuario);
    			rp.setIdProveedor(p.getId());
    			rp.setIdConsultaProveedor(cp.getId());
    			
    			rp.insert(conn);
    		}
    		
    		conn.commit();
			
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			conn = null;
			
			log.info("getProveedores output: " + rpData.toString());

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
		} catch (NamingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
    	
    	return rpData;
	}

	@GET
	@Path("/getPromocion")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public ImageData getPromocion() {
		
		ImageData imData;
		java.sql.Connection conn;
		ArrayList<AbstractMap.SimpleEntry<String, String>> listParameters;
		
		imData = null;
		conn = null;
		
    	try {
    		log.info("getPromocion");
    		
    		imData = new ImageData();
    		
    		conn = getConnection(true);
    		
    		File file = new File("/home/lfhernandez/Pictures/mecanicos_a_domicilio.png");
    		
            // Reading a Image file from file system
            FileInputStream imageInFile = new FileInputStream(file);
            byte imageData[] = new byte[(int) file.length()];
            imageInFile.read(imageData);
 
            // Converting Image byte array into Base64 String
            imData.setData(Base64.encodeBase64URLSafeString(imageData));
            
            imData.setId(1);
            
            Vigencia v = new Vigencia();
            v.setInicio("2015-04-01");
            v.setFin("2015-04-30");
            
            imData.setVigencia(v);
    		
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

    		log.info("getPromocion output: " + imData.toString());

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
		} catch (NamingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
    	
    	return imData;
	}

	@PUT
	@Path("/receive")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.TEXT_PLAIN)
	public String putTodo(JAXBElement<CarData> todo) {
		
		
		CarData carData;
		java.sql.Connection conn;
		
		carData = null;
		conn = null;
		
    	try {
    		// cargo archivo de configuracion
    		conn = getConnection(true);
			
			carData = todo.getValue();
			
			log.info("receive input: " + carData.toString());
			
			
			System.out.println(todo);
			
			conn.setAutoCommit(false);

			carData.save(conn);
			
			if (!carData.getUsuarios().getUsuarios().isEmpty()) {
				
				InfoSincro is = new InfoSincro();
				
				is.setSentido((byte) InfoSincro.tipoSincro.PHONE_TO_SERVER.getCode());
				is.setUsuarioIdUsuario(carData.getUsuarios().getUsuarios().get(0).getId());
				is.setFecha(new Date());
				
				is.insert(conn);
			}
						
			conn.commit();
			
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			conn = null;
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
		} catch (NamingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
	
	private synchronized Connection getConnection(boolean pooledConnection) throws SQLException, IOException, InvalidFileFormatException, ClassNotFoundException, NamingException {
		if (pooledConnection) {
		     //pooledCount++;
	    	 //Create a datasource for pooled connections.
			//Context initContext = new InitialContext();
			// Context envContext  = (Context) initContext.lookup("java:/comp/env");
			//context.("java:/comp/env");
			//DataSource ds = (DataSource) envContext.lookup("jdbc/CarDB");
			ds = (DataSource) context.getAttribute("DBCPool");
			
			System.out.println("Context: " + context);
			System.out.println("DataSource: " + ds);
		
		    // Allocate and use a connection from the pool
		    return ds.getConnection();
		}
		else {
			Wini ini;
			
		    //nonPooledCount++;
    		// cargo archivo de configuracion
            ini = new Wini();
        	
        	
        	ini.load(CarResource.class.getResourceAsStream("/etc/config.ini")); 
        	
			// abro conexion a la BD
			Class.forName("com.mysql.jdbc.Driver");
			return DriverManager.getConnection("jdbc:mysql://" + ini.get("DB", "host") + ":3306/" + ini.get("DB", "database"), 
        			ini.get("DB", "user"), ini.get("DB", "password"));
		  
		}
	}
	
}
