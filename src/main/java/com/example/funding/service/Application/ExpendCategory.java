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

    public default ExpendCategory getExpendCategory(int categoryCode) {
        switch (categoryCode) {
            case 0: return ExpendCategory.Office.officeSupplies;
            case 1: return ExpendCategory.Office.pen;
            case 2: return ExpendCategory.Office.notebook;
            case 3: return ExpendCategory.Print.print;
            case 4: return ExpendCategory.Print.paper;
            case 5: return ExpendCategory.Maintenance.building;
            case 6: return ExpendCategory.Maintenance.instrument;
            case 7: return ExpendCategory.Maintenance.publicSever;
            case 8: return ExpendCategory.Postage.postage;
            case 9: return ExpendCategory.Postage.telephone;
            case 10: return ExpendCategory.Train.train;
            default: return ExpendCategory.Error.noSuchCategory;
        }
    }
    public default int getValueOfExpend(ExpendCategory expendCategory){
        if (ExpendCategory.Office.officeSupplies.equals(expendCategory)) {
            return 0;
        } else if (ExpendCategory.Office.pen.equals(expendCategory)) {
            return 1;
        } else if (ExpendCategory.Office.notebook.equals(expendCategory)) {
            return 2;
        } else if (ExpendCategory.Print.print.equals(expendCategory)) {
            return 3;
        } else if (ExpendCategory.Print.paper.equals(expendCategory)) {
            return 4;
        } else if (ExpendCategory.Maintenance.building.equals(expendCategory)) {
            return 5;
        } else if (ExpendCategory.Maintenance.instrument.equals(expendCategory)) {
            return 6;
        } else if (ExpendCategory.Maintenance.publicSever.equals(expendCategory)) {
            return 7;
        } else if (ExpendCategory.Postage.postage.equals(expendCategory)) {
            return 8;
        } else if (ExpendCategory.Postage.telephone.equals(expendCategory)) {
            return 9;
        } else if (ExpendCategory.Train.train.equals(expendCategory)) {
            return 10;
        }
        return 11;
    }


}


// Nested enum inside an enum
//public enum ExpendCategory {
//    OFFICE(Office.class),
//    PRINT(Print.class),
//    MAINTENANCE(Maintenance.class),
//    POSTAGE(Postage.class),
//    TRAIN(Train.class),
//    ERROR(Error.class);
//
//    private final Class<? extends Subcategory> subcategory;
//
//    private ExpendCategory(Class<? extends Subcategory> subcategory) {
//        this.subcategory = subcategory;
//    }
//
//    public Class<? extends Subcategory> getSubcategory() {
//        return subcategory;
//    }
//
//    public interface Subcategory {}
//
//    public enum Office implements Subcategory {
//        OFFICE_SUPPLIES, PEN, NOTEBOOK;
//    }
//
//    public enum Print implements Subcategory {
//        PRINT, PAPER;
//    }
//
//    public enum Maintenance implements Subcategory {
//        BUILDING, INSTRUMENT, PUBLIC_SERVER;
//    }
//
//    public enum Postage implements Subcategory {
//        POSTAGE, TELEPHONE;
//    }
//
//    public enum Train implements Subcategory {
//        TRAIN;
//    }
//
//    public enum Error implements Subcategory {
//        NO_SUCH_CATEGORY;
//    }
//}


