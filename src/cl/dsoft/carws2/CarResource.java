package cl.dsoft.carws2;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
//import org.apache.tomcat.util.ExceptionUtils;
//import org.apache.commons.lang.exception.ExceptionUtils;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.Wini;
import org.joda.time.DateTime;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.codec.binary.Base64;

import cl.dsoft.car.misc.UnsupportedParameterException;
//import cl.dsoft.car.server.model.MantencionBaseHechaModelo;
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
	@Produces({MediaType.APPLICATION_XML + "; charset=ISO-8859-1", MediaType.APPLICATION_JSON})
	public CarData getCarNewsByIdUsuario(
			@PathParam("idUsuario") Long idUsuario,
		    @PathParam("fechaModificacion") String fechaModificacion) {
		
		CarData carData;
		java.sql.Connection conn;
		String fm;
    	ArrayList<AbstractMap.SimpleEntry<String, String>> listParameters;
    	ArrayList<InfoSincro> lstInfoSincro;
		
		carData = null;
		conn = null;
		
    	try {
    		
    		fm = URLDecoder.decode(fechaModificacion, "UTF-8");
    		
    		log.info("byIdUsuario input: " + String.valueOf(idUsuario) + "/" + fm);
    		conn = getConnection(true);
    		
    		listParameters = new ArrayList<AbstractMap.SimpleEntry<String, String>>();
			/*
    		Statement stmt = conn.createStatement();
            
            ResultSet rs = stmt.executeQuery("SHOW VARIABLES LIKE 'c%'");
            
            while (rs.next()) {
                System.out.println(rs.getString(1) + " " + rs.getString(2));
            }
            
            rs.close();
            
            stmt.close();
			*/
    		
    		Usuario u = Usuario.getById(conn, String.valueOf(idUsuario));
    		
    		if (!fm.equals("1900-01-01")) {
    			// busco la sincro mas reciente de este tipo
    			// 2015-06-29 se ignora la fecha/hora enviada desde la app movil, ya que puede estar desincronizada con el servidor
				
				listParameters.add(new AbstractMap.SimpleEntry<String, String>("id_usuario", String.valueOf(u.getId())));
				listParameters.add(new AbstractMap.SimpleEntry<String, String>("sentido", String.valueOf(InfoSincro.tipoSincro.SERVER_TO_PHONE.getCode())));
				
				lstInfoSincro = InfoSincro.seek(conn, listParameters, "fecha", "DESC", 0, 1);
				
				if (!lstInfoSincro.isEmpty()) {
					InfoSincro is = lstInfoSincro.get(0);
					
					fm = is.getFecha();
				}
				else {
					fm = "1900-01-01";
				}
    				
    		}
    		
            carData = new CarData(conn, u.getId(), fm);
			
			if (carData.hasData()) {
				// 2015-06-29 solamente actualizo
				listParameters.clear();
				listParameters.add(new AbstractMap.SimpleEntry<String, String>("id_usuario", String.valueOf(u.getId())));
				listParameters.add(new AbstractMap.SimpleEntry<String, String>("sentido", String.valueOf(InfoSincro.tipoSincro.SERVER_TO_PHONE.getCode())));
				
				lstInfoSincro = InfoSincro.seek(conn, listParameters, "fecha", "DESC", 0, 1);
				
				if (!lstInfoSincro.isEmpty()) {
					InfoSincro is = lstInfoSincro.get(0);
					
					is.setFecha(InfoSincro.getTimeFromServer(conn));
					
					is.update(conn);
				}
				else {
					InfoSincro is = new InfoSincro();
					
					is.setSentido((byte) InfoSincro.tipoSincro.SERVER_TO_PHONE.getCode());
					is.setUsuarioIdUsuario(u.getId());
					is.setFecha(InfoSincro.getTimeFromServer(conn));
					
					is.insert(conn);
				}
			}
			
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				log.error(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
			}

			conn = null;
			
			log.info("byIdUsuario output: " + carData.toString());
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
		} catch (InvalidFileFormatException e) {
			// TODO Auto-generated catch block
			log.error(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			log.error(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			log.error(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
		} catch (UnsupportedParameterException e) {
			// TODO Auto-generated catch block
			log.error(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					log.error(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
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
    	ArrayList<AbstractMap.SimpleEntry<String, String>> listParameters;
    	ArrayList<InfoSincro> lstInfoSincro;
		
		carData = null;
		conn = null;
		
    	try {
    		log.info("byIdRedSocial input: " + String.valueOf(idRedSocial) + "/" + token);
    		conn = getConnection(true);
    		
    		listParameters = new ArrayList<AbstractMap.SimpleEntry<String, String>>();
			
			carData = new CarData(conn, idRedSocial, token, true);
			
			if (!carData.getUsuarios().getUsuarios().isEmpty()) {
				// 2015-06-29 solamente actualizo
				
				Usuario u = carData.getUsuarios().getUsuarios().get(0);
				
				listParameters.clear();
				listParameters.add(new AbstractMap.SimpleEntry<String, String>("id_usuario", String.valueOf(u.getId())));
				listParameters.add(new AbstractMap.SimpleEntry<String, String>("sentido", String.valueOf(InfoSincro.tipoSincro.SERVER_TO_PHONE.getCode())));
				
				lstInfoSincro = InfoSincro.seek(conn, listParameters, "fecha", "DESC", 0, 1);
				
				if (!lstInfoSincro.isEmpty()) {
					InfoSincro is = lstInfoSincro.get(0);
					
					is.setFecha(InfoSincro.getTimeFromServer(conn));
					
					is.update(conn);
				}
				else {
					InfoSincro is = new InfoSincro();
					
					is.setSentido((byte) InfoSincro.tipoSincro.SERVER_TO_PHONE.getCode());
					is.setUsuarioIdUsuario(u.getId());
					is.setFecha(InfoSincro.getTimeFromServer(conn));
					
					is.insert(conn);
				}
			}
			
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				log.error(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
			}

			conn = null;

			log.info("byIdRedSocial output: " + carData.toString());

    	} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			log.error(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
		} catch (InvalidFileFormatException e) {
			// TODO Auto-generated catch block
			log.error(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			log.error(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
		} catch (UnsupportedParameterException e) {
			// TODO Auto-generated catch block
			log.error(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
		}finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					log.error(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
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
				
				InfoSincro is = new InfoSincro();
				
				is.setSentido((byte) InfoSincro.tipoSincro.SERVER_TO_PHONE.getCode());
				is.setUsuarioIdUsuario(u.getId());
				is.setFecha(InfoSincro.getTimeFromServer(conn));
				
				is.insert(conn);
				
				conn.commit();

			}			
			
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				log.error(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
			}

			conn = null;
			
			log.info("createUser output: " + carData.toString());

    	} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			log.error(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
		} catch (InvalidFileFormatException e) {
			// TODO Auto-generated catch block
			log.error(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			log.error(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
		} finally {
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					log.error(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
				}

				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					log.error(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
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
    		conn = getConnection(true);
    		
    		RespuestaProveedor rp;
    		
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
				log.error(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
			}

			conn = null;
			
			log.info("getProveedores output: " + rpData.toString());

    	} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			log.error(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
		} catch (InvalidFileFormatException e) {
			// TODO Auto-generated catch block
			log.error(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			log.error(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
		} finally {
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					log.error(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
				}

				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					log.error(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
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
    		conn = getConnection(true);
    		
    		imData = new ImageData();
    		
    		
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
				log.error(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
			}

    		log.info("getPromocion output: " + imData.toString());

    	} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			log.error(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
		} catch (InvalidFileFormatException e) {
			// TODO Auto-generated catch block
			log.error(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			log.error(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
		} finally {
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					log.error(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
				}

				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					log.error(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
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
    	ArrayList<AbstractMap.SimpleEntry<String, String>> listParameters;
    	ArrayList<InfoSincro> lstInfoSincro;
		
		carData = null;
		conn = null;
		
    	try {
    		
			carData = todo.getValue();
			
			log.info("receive input: " + carData.toString());
			conn = getConnection(true);
			
			listParameters = new ArrayList<AbstractMap.SimpleEntry<String, String>>();
			
			System.out.println(todo);
			
			conn.setAutoCommit(false);

			carData.save(conn);
			
			if (carData.hasData()) {
				/*
				InfoSincro is = null;
				
	    		listParameters = new ArrayList<AbstractMap.SimpleEntry<String, String>>();

				listParameters.add(new AbstractMap.SimpleEntry<String, String>("id_usuario", String.valueOf(idUsuario)));
				listParameters.add(new AbstractMap.SimpleEntry<String, String>("sentido", String.valueOf(InfoSincro.tipoSincro.PHONE_TO_SERVER.getCode())));
				
				lstInfoSincro = InfoSincro.seek(conn, listParameters, "fecha", "DESC", 0, 1);
				
				if (!lstInfoSincro.isEmpty()) {
					InfoSincro is = lstInfoSincro.get(0);
					
					is.setFecha(InfoSincro.getTimeFromServer(conn));
					
					is.update(conn);
				}
				else {
					InfoSincro is = new InfoSincro();
					
					is.setSentido((byte) InfoSincro.tipoSincro.PHONE_TO_SERVER.getCode());
					is.setUsuarioIdUsuario(idUsuario);
					is.setFecha(InfoSincro.getTimeFromServer(conn));
					
					is.insert(conn);
				}
				*/
				// 2015-06-29 solamente actualizo
				
				Long idUsuario = carData.getIdUsuario();
				
				if (idUsuario != null) {
				
					Usuario u = Usuario.getById(conn, String.valueOf(idUsuario));
					
					listParameters.clear();
					listParameters.add(new AbstractMap.SimpleEntry<String, String>("id_usuario", String.valueOf(u.getId())));
					listParameters.add(new AbstractMap.SimpleEntry<String, String>("sentido", String.valueOf(InfoSincro.tipoSincro.PHONE_TO_SERVER.getCode())));
					
					lstInfoSincro = InfoSincro.seek(conn, listParameters, "fecha", "DESC", 0, 1);
					
					if (!lstInfoSincro.isEmpty()) {
						InfoSincro is = lstInfoSincro.get(0);
						
						is.setFecha(InfoSincro.getTimeFromServer(conn));
						
						is.update(conn);
					}
					else {
						InfoSincro is = new InfoSincro();
						
						is.setSentido((byte) InfoSincro.tipoSincro.PHONE_TO_SERVER.getCode());
						is.setUsuarioIdUsuario(u.getId());
						is.setFecha(InfoSincro.getTimeFromServer(conn));
						
						is.insert(conn);
					}
				}
				
			}
						
			conn.commit();
			
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				log.error(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
			}

			conn = null;
		    //return "ok"; //putAndGetResponse(null);
			
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			log.error(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
		} catch (InvalidFileFormatException e) {
			// TODO Auto-generated catch block
			log.error(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			log.error(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
		} catch (UnsupportedParameterException e) {
			// TODO Auto-generated catch block
			log.error(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
		} finally {
			if (conn != null) {
				
				try {
					conn.rollback();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					log.error(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
				}
				
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					log.error(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
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
