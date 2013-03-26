/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.sql.Timestamp;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.ImageTransformer;
import models.Marker;
import models.MarkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class IndexController {
    
    private String imagePath = "../images/";
    
    final int imageWidth = 300;

    @Autowired
    MarkerService markerService;

    @RequestMapping(value = "/")
    public String go() {
        return "redirect:start";
    }

    @RequestMapping(value = "/start/{id}", method = RequestMethod.GET)
    public String indexGetById(@PathVariable Long id, HttpServletRequest request, ModelMap model) {

        model.addAttribute("marker", markerService.getById(id));
        return "index";

    }

    @RequestMapping(value = "/start", method = RequestMethod.GET)
    public String indexGet(ModelMap model) {

        model.addAttribute("markers", markerService.getList());
        return "index";

    }

    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String markerGetById(@PathVariable Long id, ModelMap model) {

        model.addAttribute("marker", markerService.getById(id));
        return "marker";

    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String markerDeleteById(@PathVariable Long id, ModelMap model) {

        Marker m = markerService.getById(id);
        File f = new File(imagePath + m.getUrl());
        f.delete();
        markerService.deleteById(id);
        
        return "redirect:/start";

    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String listGet(ModelMap model) {

        model.addAttribute("list", markerService.getList());
        return "list";

    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String getForm(ModelMap model) {
        return "form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String sendData(@RequestParam("north") Double north, @RequestParam("east") Double east, @RequestParam("phone") String phone, @RequestParam("image") MultipartFile file, HttpServletRequest request, ModelMap model) {

        if (!"image/jpg".equals(file.getContentType()) && !"image/jpeg".equals(file.getContentType())) {
            return "redirect:/form";
        }
        
        Date date = new Date();
        Timestamp ts = new Timestamp(date.getTime());
        String filename = ts.toString();
        filename = filename.replaceAll("\\s", "_");
        String path = imagePath + filename;

        File imageFile = new File(path);
        
        try {
            imageFile.getParentFile().createNewFile();
            BufferedImage image = ImageIO.read(file.getInputStream());
            image = ImageTransformer.scaleByWidth(image, imageWidth);
            imageFile.createNewFile();
            ImageIO.write(image, "jpg", imageFile);
            
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/form";
        }
        
        Marker m = new Marker();
        m.setNorth(north);
        m.setEast(east);
        m.setPhone(phone);
        m.setUrl(filename);
        
        markerService.add(m);
        
        return "redirect:/start";
    }
    
    @RequestMapping(value = "/images/{id}", method = RequestMethod.GET, produces="image/jpg")
    public @ResponseBody byte[] getImage(@PathVariable Long id, HttpServletResponse response) throws IOException {
       
        Marker m = markerService.getById(id);
        BufferedImage image = ImageIO.read(new File(imagePath + m.getUrl()));
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", bao);
        return bao.toByteArray();
        
    }
    
}
