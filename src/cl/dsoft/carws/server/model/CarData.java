package cl.dsoft.carws.server.model;

import java.sql.SQLException;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import cl.dsoft.carws.server.db.MantencionUsuario;
import cl.dsoft.carws.server.db.MantencionUsuarioHecha;
import cl.dsoft.carws.server.db.Recordatorio;
import cl.dsoft.carws.server.db.Rendimiento;
import cl.dsoft.carws.server.db.Reparacion;
import cl.dsoft.carws.server.db.Usuario;
import cl.dsoft.carws.server.db.Vehiculo;

@XmlRootElement(name = "CarData")
//If you want you can define the order in which the fields are written
//Optional
@XmlType(propOrder = { "usuarios", "vehiculos", "mantencionUsuarios", "mantencionUsuarioHechas", "recordatorios", "rendimientos", "reparaciones" })
public class CarData {

	protected Usuarios usuarios;
	protected Vehiculos vehiculos;
	protected MantencionUsuarios mantencionUsuarios;
	protected MantencionUsuarioHechas mantencionUsuarioHechas;
	protected Recordatorios recordatorios;
	protected Rendimientos rendimientos;
	protected Reparaciones reparaciones;
	
	public CarData() {

		this.usuarios = null;
		this.vehiculos = null;
		this.mantencionUsuarios = null;
		this.mantencionUsuarioHechas = null;
		this.recordatorios = null;
		this.rendimientos = null;
		this.reparaciones = null;
	}

	public CarData(java.sql.Connection conn, Long idUsuario, String fechaModificacion) {

		this.usuarios = new Usuarios(conn, idUsuario, fechaModificacion);
		this.vehiculos = new Vehiculos(conn, idUsuario, fechaModificacion);
		this.mantencionUsuarios = new MantencionUsuarios(conn, idUsuario, fechaModificacion);
		this.mantencionUsuarioHechas = new MantencionUsuarioHechas(conn, idUsuario, fechaModificacion);
		this.recordatorios = new Recordatorios(conn, idUsuario, fechaModificacion);
		this.rendimientos = new Rendimientos(conn, idUsuario, fechaModificacion);
		this.reparaciones = new Reparaciones(conn, idUsuario, fechaModificacion);
	}

	public CarData(java.sql.Connection conn, Long idRedSocial, Long token) {

		this.usuarios = new Usuarios(conn, idRedSocial, token);
		
		if (!this.usuarios.getUsuarios().isEmpty()) {
			Usuario u = this.getUsuarios().getUsuarios().get(0);
			
			this.vehiculos = new Vehiculos(conn, u.getId(), "1900-01-01");
			this.mantencionUsuarios = new MantencionUsuarios(conn, u.getId(), "1900-01-01");
			this.mantencionUsuarioHechas = new MantencionUsuarioHechas(conn, u.getId(), "1900-01-01");
			this.recordatorios = new Recordatorios(conn, u.getId(), "1900-01-01");
			this.rendimientos = new Rendimientos(conn, u.getId(), "1900-01-01");
			this.reparaciones = new Reparaciones(conn, u.getId(), "1900-01-01");
		}
		else {
			
			this.vehiculos = new Vehiculos();
			this.mantencionUsuarios = new MantencionUsuarios();
			this.mantencionUsuarioHechas = new MantencionUsuarioHechas();
			this.recordatorios = new Recordatorios();
			this.rendimientos = new Rendimientos();
			this.reparaciones = new Reparaciones();
		}
		
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
	public Rendimientos getRendimientos() {
		return rendimientos;
	}

	/**
	 * @param rendimientos the rendimientos to set
	 */
	public void setRendimientos(Rendimientos rendimientos) {
		this.rendimientos = rendimientos;
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
	
		for (MantencionUsuario mantencionUsuario : this.mantencionUsuarios.getMantencionUsuarios()) {
			
			mantencionUsuario.save(conn);
		}

		for (MantencionUsuarioHecha mantencionUsuarioHecha : this.mantencionUsuarioHechas.getMantencionUsuarioHechas()) {
			
			mantencionUsuarioHecha.save(conn);
		}

		for (Recordatorio recordatorio : this.recordatorios.getRecordatorios()) {
			
			recordatorio.save(conn);
		}

		for (Rendimiento rendimiento : this.rendimientos.getRendimientos()) {
			
			rendimiento.save(conn);
		}

		for (Reparacion reparacion : this.reparaciones.getReparaciones()) {
			
			reparacion.save(conn);
		}
	}
}
