package com.pabloagustin.springbootdatajpa.controllers;


import com.pabloagustin.springbootdatajpa.models.Cliente;
import com.pabloagustin.springbootdatajpa.models.Factura;
import com.pabloagustin.springbootdatajpa.models.ItemFactura;
import com.pabloagustin.springbootdatajpa.models.Producto;
import com.pabloagustin.springbootdatajpa.service.IClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/factura")
// Importante agregar la anotacion
@SessionAttributes("factura")
public class FacturaController {

    @Autowired
    private IClienteService clienteService;

    @GetMapping("/ver/{id}")
    public String ver(@PathVariable(value = "id") Long id,
                      Model model,
                      RedirectAttributes flash){

        // Obtenemos la factura por el ID

        Factura factura = clienteService.findFacturaById(id);

        // Corroboramos que la factura existe
        if(factura == null){
            flash.addFlashAttribute("error", "La factura no existe en la BD");
            return "redirect:/listar";
        }

        // Si existe, mostramos la factura en la vista
        model.addAttribute("factura", factura);
        model.addAttribute("titulo", "Factura: ".concat(factura.getDescripcion()));

        // Retornamos la vista
        return "factura/ver";
    }

    @GetMapping("/form/{clienteId}")
    public String crear(@PathVariable(value = "clienteId") Long clienteId, Model model, RedirectAttributes flash){
        // Obtener cliente por su ID
        Cliente cliente = clienteService.findOne(clienteId);
        // El cliente es nulo? Redirigimos al listado de clientes con msj de error
        if(cliente == null){
            flash.addAttribute("error", "El cliente existe en la base de datos");
            return "redirect:/listar";
        }

        // Asignamos el cliente a una factura. Creamos la factura
        Factura factura = new Factura();
        factura.setCliente(cliente);

        model.addAttribute("factura", factura);
        model.addAttribute("titulo", "Crear Factura");

        return "factura/form";
    }

    @GetMapping(value = "/cargar-productos/{term}", produces = {"application/json"})
    public @ResponseBody List<Producto> cargarProductos(@PathVariable String term){
        return clienteService.findByNombre(term);
    }

    // Metodo guardar para persistencia de factura en BD
    @PostMapping("/form")
    public String guardar(@Valid Factura factura,
                          BindingResult result,
                          Model model,
                          @RequestParam(name="item_id[]", required = false) Long[] itemId,
                          @RequestParam(name="cantidad[]", required = false) Integer[] cantidad,
                          RedirectAttributes flash,
                          SessionStatus status){

        if(result.hasErrors()){
            model.addAttribute("titulo", "Crear Factura");
            return "factura/form";
        }

        if(itemId == null || itemId.length == 0){
            model.addAttribute("titulo", "Crear Factura");
            model.addAttribute("error", "Error: La factura NO puede no tener lineas!");
            return "factura/form";
        }

        for (int i = 0; i < itemId.length; i++){
            Producto producto = clienteService.findProductoById(itemId[i]);

            ItemFactura linea = new ItemFactura();
            linea.setCantidad(cantidad[i]);
            linea.setProducto(producto);
            factura.addItemFactura(linea);
        }

        // Guardamos la factura en la BD
        clienteService.saveFactura(factura);
        // Finalizamos la sesion luego de haber guardado la factura en la BD
        status.setComplete();
        flash.addFlashAttribute("success", "Factura creada con EXITO!");
        return "redirect:/ver/" + factura.getCliente().getId();
    }

    // Implementamos el metodo eliminar
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash){

        Factura factura = clienteService.findFacturaById(id);

        if (factura != null){
            clienteService.deleteFactura(id);
            flash.addFlashAttribute("success", "La factura se ha eliminada correctamente!");
            return "redirect:/ver/" + factura.getCliente().getId();
        }

        flash.addFlashAttribute("error", "La factura no existe en la base de datos, no se pudo eliminar");
        return "redirect:/listar";
    }

}
