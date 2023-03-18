package web;
import java.util.ArrayList;
import java.util.List;
import metier.Categorie;
public class CategorieModele {
List<Categorie> categories = new ArrayList<>();
String nom;
public List<Categorie> getCategories() {
return categories;
}
public void setCategories(List<Categorie> categories) {
this.categories = categories;
}
public void setCat(String nom) {
	this.nom = nom;
}
}