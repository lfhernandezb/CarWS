package cl.dsoft.carws.server.model;

import java.sql.SQLException;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import cl.dsoft.carws.server.db.MantencionBaseHecha;
import cl.dsoft.carws.server.db.MantencionUsuario;
import cl.dsoft.carws.server.db.MantencionUsuarioHecha;
import cl.dsoft.carws.server.db.Recordatorio;
import cl.dsoft.carws.server.db.CargaCombustible;
import cl.dsoft.carws.server.db.Reparacion;
import cl.dsoft.carws.server.db.Usuario;
import cl.dsoft.carws.server.db.Vehiculo;

@XmlRootElement(name = "CarData")
//If you want you can define the order in which the fields are written
//Optional
@XmlType(propOrder = { "paises", "regiones", "comunas", "usuarios", "vehiculos", "mantencionBaseHechas", "mantencionUsuarios", "mantencionUsuarioHechas", "recordatorios", "cargaCombustibles", "reparaciones" })
public class CarData {

	protected Paises paises;
	protected Regiones regiones;
	protected Comunas comunas;
	protected Usuarios usuarios;
	protected Vehiculos vehiculos;
	protected MantencionBaseHechas mantencionBaseHechas;
	protected MantencionUsuarios mantencionUsuarios;
	protected MantencionUsuarioHechas mantencionUsuarioHechas;
	protected Recordatorios recordatorios;
	protected CargaCombustibles cargaCombustibles;
	protected Reparaciones reparaciones;
	
	public CarData() {

		this.paises = null;
		this.regiones = null;
		this.comunas = null;
		this.usuarios = null;
		this.vehiculos = null;
		this.mantencionBaseHechas = null;
		this.mantencionUsuarios = null;
		this.mantencionUsuarioHechas = null;
		this.recordatorios = null;
		this.cargaCombustibles = null;
		this.reparaciones = null;
	}

	public CarData(java.sql.Connection conn, Long idUsuario, String fechaModificacion) {

		this.paises = new Paises(conn, idUsuario, fechaModificacion);
		this.regiones = new Regiones(conn, idUsuario, fechaModificacion);
		this.comunas = new Comunas(conn, idUsuario, fechaModificacion);
		this.usuarios = new Usuarios(conn, idUsuario, fechaModificacion);
		this.vehiculos = new Vehiculos(conn, idUsuario, fechaModificacion);
		this.mantencionBaseHechas = new MantencionBaseHechas(conn, idUsuario, fechaModificacion);
		this.mantencionUsuarios = new MantencionUsuarios(conn, idUsuario, fechaModificacion);
		this.mantencionUsuarioHechas = new MantencionUsuarioHechas(conn, idUsuario, fechaModificacion);
		this.recordatorios = new Recordatorios(conn, idUsuario, fechaModificacion);
		this.cargaCombustibles = new CargaCombustibles(conn, idUsuario, fechaModificacion);
		this.reparaciones = new Reparaciones(conn, idUsuario, fechaModificacion);
	}

	public CarData(java.sql.Connection conn, Long idRedSocial, Long token) {

		this.usuarios = new Usuarios(conn, idRedSocial, token);
		
		if (!this.usuarios.getUsuarios().isEmpty()) {
			Usuario u = this.getUsuarios().getUsuarios().get(0);
			
			this.paises = new Paises(conn, u.getId(), "1900-01-01");
			this.regiones = new Regiones(conn, u.getId(), "1900-01-01");
			this.comunas = new Comunas(conn, u.getId(), "1900-01-01");
			this.vehiculos = new Vehiculos(conn, u.getId(), "1900-01-01");
			this.mantencionBaseHechas = new MantencionBaseHechas(conn, u.getId(), "1900-01-01");
			this.mantencionUsuarios = new MantencionUsuarios(conn, u.getId(), "1900-01-01");
			this.mantencionUsuarioHechas = new MantencionUsuarioHechas(conn, u.getId(), "1900-01-01");
			this.recordatorios = new Recordatorios(conn, u.getId(), "1900-01-01");
			this.cargaCombustibles = new CargaCombustibles(conn, u.getId(), "1900-01-01");
			this.reparaciones = new Reparaciones(conn, u.getId(), "1900-01-01");
		}
		else {
			
			this.paises = new Paises();
			this.regiones = new Regiones();
			this.comunas = new Comunas();
			this.vehiculos = new Vehiculos();
			this.mantencionBaseHechas = new MantencionBaseHechas();
			this.mantencionUsuarios = new MantencionUsuarios();
			this.mantencionUsuarioHechas = new MantencionUsuarioHechas();
			this.recordatorios = new Recordatorios();
			this.cargaCombustibles = new CargaCombustibles();
			this.reparaciones = new Reparaciones();
		}
		
	}

	/**
	 * @return the paises
	 */
	public Paises getPaises() {
		return paises;
	}

	/**
	 * @param paises the paises to set
	 */
	public void setPaises(Paises paises) {
		this.paises = paises;
	}

	/**
	 * @return the regiones
	 */
	public Regiones getRegiones() {
		return regiones;
	}

	/**
	 * @param regiones the regiones to set
	 */
	public void setRegiones(Regiones regiones) {
		this.regiones = regiones;
	}

	/**
	 * @return the comunas
	 */
	public Comunas getComunas() {
		return comunas;
	}

	/**
	 * @param comunas the comunas to set
	 */
	public void setComunas(Comunas comunas) {
		this.comunas = comunas;
	}

	/**
	 * @return the usuarios
	 */
	public Usuarios getUsuarios() {
		return usuarios;
	}

	/**
	 * @param usuarios the usuarios to set
	 */
	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

	/**
	 * @return the vehiculos
	 */
	public Vehiculos getVehiculos() {
		return vehiculos;
	}

	/**
	 * @param vehiculos the vehiculos to set
	 */
	public void setVehiculos(Vehiculos vehiculos) {
		this.vehiculos = vehiculos;
	}
	
	/**
	 * @return the mantencionBaseHechas
	 */
	public MantencionBaseHechas getMantencionBaseHechas() {
		return mantencionBaseHechas;
	}

	/**
	 * @param mantencionBaseHechas the mantencionBaseHechas to set
	 */
	public void setMantencionBaseHechas(
			MantencionBaseHechas mantencionBaseHechas) {
		this.mantencionBaseHechas = mantencionBaseHechas;
	}

	/**
	 * @return the mantencionUsuarios
	 */
	public MantencionUsuarios getMantencionUsuarios() {
		return mantencionUsuarios;
	}

	/**
	 * @param mantencionUsuarios the mantencionUsuarios to set
	 */
	public void setMantencionUsuarios(MantencionUsuarios mantencionUsuarios) {
		this.mantencionUsuarios = mantencionUsuarios;
	}

	/**
	 * @return the mantencionUsuarioHechas
	 */
	public MantencionUsuarioHechas getMantencionUsuarioHechas() {
		return mantencionUsuarioHechas;
	}

	/**
	 * @param mantencionUsuarioHechas the mantencionUsuarioHechas to set
	 */
	public void setMantencionUsuarioHechas(
			MantencionUsuarioHechas mantencionUsuarioHechas) {
		this.mantencionUsuarioHechas = mantencionUsuarioHechas;
	}

	/**
	 * @return the recordatorios
	 */
	public Recordatorios getRecordatorios() {
		return recordatorios;
	}

	/**
	 * @param recordatorios the recordatorios to set
	 */
	public void setRecordatorios(Recordatorios recordatorios) {
		this.recordatorios = recordatorios;
	}

	/**
	 * @return the rendimientos
	 */
	public CargaCombustibles getCargaCombustibles() {
		return cargaCombustibles;
	}

	/**
	 * @param rendimientos the rendimientos to set
	 */
	public void setCargaCombustibles(CargaCombustibles cargaCombustibles) {
		this.cargaCombustibles = cargaCombustibles;
	}

	/**
	 * @return the reparaciones
	 */
	public Reparaciones getReparaciones() {
		return reparaciones;
	}

	/**
	 * @param reparaciones the reparaciones to set
	 */
	public void setReparaciones(Reparaciones reparaciones) {
		this.reparaciones = reparaciones;
	}
	
	public void save(java.sql.Connection conn) throws SQLException {
		
		for (Usuario usuario : this.usuarios.getUsuarios()) {
			
			usuario.save(conn);
		}

		for (Vehiculo vehiculo : this.vehiculos.getVehiculos()) {
			
			vehiculo.save(conn);
		}
	
		for (MantencionBaseHecha mantencionBaseHecha : this.mantencionBaseHechas.getMantencionBaseHechas()) {
			
			mantencionBaseHecha.save(conn);
		}

		for (MantencionUsuario mantencionUsuario : this.mantencionUsuarios.getMantencionUsuarios()) {
			
			mantencionUsuario.save(conn);
		}

		for (MantencionUsuarioHecha mantencionUsuarioHecha : this.mantencionUsuarioHechas.getMantencionUsuarioHechas()) {
			
			mantencionUsuarioHecha.save(conn);
		}

		for (Recordatorio recordatorio : this.recordatorios.getRecordatorios()) {
			
			recordatorio.save(conn);
		}

		for (CargaCombustible rendimiento : this.cargaCombustibles.getCargaCombustibles()) {
			
			rendimiento.save(conn);
		}

		for (Reparacion reparacion : this.reparaciones.getReparaciones()) {
			
			reparacion.save(conn);
		}
	}
}
