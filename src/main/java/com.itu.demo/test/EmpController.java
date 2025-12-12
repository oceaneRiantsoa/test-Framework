package com.itu.demo.test;

import com.itu.demo.annotations.GetMapping;
import com.itu.demo.annotations.PostMapping;
import com.itu.demo.ModelView;
import com.itu.demo.model.Emp;

public class EmpController {
    @GetMapping("/emp/form")
    public ModelView showForm() {
        return new ModelView("empForm.jsp");
    }

    @PostMapping("/emp/save")
    public ModelView save(Emp e, int i) {
        ModelView mv = new ModelView("empResult.jsp");
        mv.addItem("emp", e);
        mv.addItem("message", "Emp enregistré avec i=" + i);
        mv.addItem("i", i);
        return mv;
    }
}