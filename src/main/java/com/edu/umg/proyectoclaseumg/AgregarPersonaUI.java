package com.edu.umg.proyectoclaseumg;

import com.edu.umg.DTO.PersonaDTO;
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
    private PersonaDTO persona;
    private PersonaDTO personaModify;
    private List<PersonaDTO> list;
    private boolean mostrarDatos = false;
    
    
    @PostConstruct
    public void init() {
        persona = new PersonaDTO();
        dml = new DMLBdd();
        personaModify = new PersonaDTO();
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
        } catch (SQLException e) {
            e.printStackTrace();
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
            persona = new PersonaDTO();
            cargarDatos();
            // Resetear el objeto persona para futuras inserciones
            persona = new PersonaDTO();
        } catch (SQLException ex) {
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
            // Llamar al método modificarPersona() de DMLBdd
            dml.modificarPersona(personaModify); // Utiliza personaModify para modificar

            // Actualizar la lista de personas
         
            
            // Mostrar mensaje de éxito
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Persona modificada con éxito"));
        } catch (SQLException e) {
            // Manejar la excepción y mostrar un mensaje de error
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al modificar la persona", e.getMessage()));
        }
    } else {
        // Si las validaciones fallan, mostrar un mensaje de error específico
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", validationMessage));
    }
       cargarDatos(); 
}

private String validarPersona2(PersonaDTO persona) {
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

 public void eliminarPersona(PersonaDTO persona) {
    try {
        System.out.println("Método eliminarPersona() llamado");
        DMLBdd dmlBdd2 = new DMLBdd();
        
        // Llama al método en DMLBdd para eliminar la persona
        dmlBdd2.eliminarPersona(persona); // Aquí 'persona' es el objeto PersonaDTO a eliminar
        
        cargarDatos(); // Actualiza la lista de personas o realiza otra acción necesaria
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Persona eliminada con éxito"));
    } catch (SQLException e) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al eliminar la persona", e.getMessage()));
    }
}

    
public void mostrarDialogo(PersonaDTO persona) {
    this.personaModify = persona;
    System.out.println("Mostrando diálogo para modificar persona con ID: " + persona.getIdPersona());
    System.out.println("Estado actual: " + persona.getEstado()); // Mensaje de depuración adicional
}

    
    public List<PersonaDTO> getList() {
        return list;
    }

    public void setList(List<PersonaDTO> list) {
        this.list = list;
    }

    public PersonaDTO getPersona() {
        return persona;
    }

    public void setPersona(PersonaDTO persona) {
        this.persona = persona;
    }

    public PersonaDTO getPersonaModify() {
        return personaModify;
    }

    public void setPersonaModify(PersonaDTO personaModify) {
        this.personaModify = personaModify;
    }
}
