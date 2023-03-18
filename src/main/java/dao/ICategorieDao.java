package dao;
import java.util.List;
import metier.Categorie;
public interface ICategorieDao {
public Categorie save(Categorie cat);
public Categorie getCategorie(Long id);
public Categorie updateCategorie(Categorie cat);
public void deleteCategorie(Long id);
public List<Categorie> getAllCategories();
public List<Categorie> categoriesParNom(Long nom);
}
