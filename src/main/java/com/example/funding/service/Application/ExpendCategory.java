package com.example.funding.service.Application;
//是application中的支出类别
public interface ExpendCategory {
    enum Office implements ExpendCategory{
        officeSupplies, pen, notebook;
    }
    enum Print implements ExpendCategory{
        print, paper;
    }
    enum Maintenance implements ExpendCategory{
        building, instrument, publicSever;
    }
    enum Postage implements ExpendCategory{
        postage, telephone;
    }
    enum Train implements ExpendCategory{
        train;
    }
    enum Error implements ExpendCategory{
        noSuchCategory;
    }

}
