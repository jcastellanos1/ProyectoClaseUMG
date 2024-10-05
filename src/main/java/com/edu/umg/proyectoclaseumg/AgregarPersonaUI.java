package com.edu.umg.proyectoclaseumg;

import com.edu.umg.DTO.persona;
import com.edu.umg.bdd.DMLBdd;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

@ManagedBean(name = "agregarPersonaUI")
@ViewScoped
public class AgregarPersonaUI implements Serializable {
    private DMLBdd dml;
    private persona persona;
    private persona personaModify;
    private List<persona> list;
    private boolean mostrarDatos = false;
//Cambios

    @PostConstruct
    public void init() {
        persona = new persona();
        dml = new DMLBdd();
        personaModify = new persona();
      //  mostrarDatos = true;
        cargarDatos();  // Llamada al método para cargar los datos al iniciar
    }
    
        public boolean isMostrarDatos() {
        return mostrarDatos;
    }
    
    public void cerrarDialogo() {
        mostrarDatos = false; // Oculta el diálogo
    }

public void cargarDatos() {
    try {
        list = dml.listaPersona();
        mostrarDatos = true; // Muestra la tabla después de cargar los datos
    } catch (Exception e) {
        e.printStackTrace();
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error al cargar datos: " + e.getMessage()));
    }
}

public void agregarPersona() {
    System.out.println("Método agregarPersona() llamado");

    if (validarPersona()) {
        try {
            DMLBdd dmlBdd = new DMLBdd();
            persona.setEstado(1);
            dmlBdd.insertarPersona(persona);

            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Persona agregada exitosamente"));
            persona = new persona();
            cargarDatos(); // Cargar los datos de nuevo
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error al agregar la persona: " + ex.getMessage()));
        }
    }
}

private boolean validarPersona() {
    boolean nombreValido = persona.getNombre() != null && !persona.getNombre().isEmpty();
    boolean apellidoValido = persona.getApellido() != null && !persona.getApellido().isEmpty();
    boolean telefonoValido = persona.getTelefono() != 0;
    boolean correoValido = persona.getCorreo() != null && !persona.getCorreo().isEmpty();

    // Verifica si todos los campos son nulos o vacíos
    if (!nombreValido || !apellidoValido || !telefonoValido || !correoValido) {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Por favor, complete todos los campos."));
        return false;
    }

    // Verifica que el teléfono tenga 8 dígitos
    if (telefonoValido && String.valueOf(persona.getTelefono()).length() != 8) {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Número de teléfono inválido. Debe tener 8 dígitos."));
        return false;
    }

    // Si todas las validaciones pasan
    return nombreValido && apellidoValido && telefonoValido && correoValido;
}

  

public void modificarPersona() {
    String validationMessage = validarPersona2(personaModify);
    if (validationMessage == null) {
        try {
            dml.modificarPersona(personaModify); // Utiliza personaModify para modificar

            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage("Persona modificada con éxito"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al modificar la persona", e.getMessage()));
        }
    } else {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", validationMessage));
    }
    cargarDatos();
}

private String validarPersona2(persona persona) {
    // Verificar que ningún campo esté vacío o sea null
    if (persona.getNombre() == null || persona.getNombre().isEmpty()) {
        return "El nombre no puede estar vacío.";
    }
    if (persona.getApellido() == null || persona.getApellido().isEmpty()) {
        return "El apellido no puede estar vacío.";
    }
    if (persona.getCorreo() == null || persona.getCorreo().isEmpty()) {
        return "El correo no puede estar vacío.";
    }
  if (persona.getEstado() != 0 && persona.getEstado() != 1) {
    return "Estado Inválido";
}
    // Verificar que el teléfono no sea 0
    if (persona.getTelefono() == 0) {
        return "El número de teléfono no puede estar vacío";
    }

    // Verificar que el teléfono tenga exactamente 8 dígitos
    if (String.valueOf(persona.getTelefono()).length() != 8) {
        return "Número de teléfono inválido. Debe tener 8 dígitos.";
    }
    
    return null; // Validación exitosa
}



public String irMostrarDatos() {
     
    return "MostrarPersona?faces-redirect=true";
}

public void eliminarPersona(persona persona) {
    try {
        DMLBdd dmlBdd2 = new DMLBdd();
        dmlBdd2.eliminarPersona(persona); // Elimina la persona

        cargarDatos(); // Actualiza la lista de personas
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage("Persona eliminada con éxito"));
    } catch (Exception e) {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al eliminar la persona", e.getMessage()));
    }
}

    
public void mostrarDialogo(persona persona) {
    this.personaModify = persona;
    System.out.println("Mostrando diálogo para modificar persona con ID: " + persona.getIdPersona());
    System.out.println("Estado actual: " + persona.getEstado()); // Mensaje de depuración adicional
}

    
    public List<persona> getList() {
        return list;
    }

    public void setList(List<persona> list) {
        this.list = list;
    }

    public persona getPersona() {
        return persona;
    }

    public void setPersona(persona persona) {
        this.persona = persona;
    }

    public persona getPersonaModify() {
        return personaModify;
    }

    public void setPersonaModify(persona personaModify) {
        this.personaModify = personaModify;
    }
}
