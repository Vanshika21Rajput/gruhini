/*package com.example.Gruhani.service;

import com.example.Gruhani.Repositories.ProductRepo;
import com.example.Gruhani.Repositories.SellerRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;

@Service
public class addproduct_db {

    @Autowired
    SellerRepo sr;
    @Autowired
    ProductRepo prepo;
    Directory dir;
    {
        try {
            dir = FSDirectory.open(Path.of("C:/Users/HP/Documents/Lucene_product"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /*public String saveproduct(productdto pdto,  OpenSearchClient client) throws IOException {
        System.out.println("inside add prodict");
        Map<String, Object> response = new HashMap<>();


                Map<String, Object> jsonMap = new HashMap<>();
                jsonMap.put("name", pdto.getName());
                jsonMap.put("price", pdto.getPrice());
                jsonMap.put("category", pdto.getCategory());
                jsonMap.put("subcategory", pdto.getSubcategory());
                jsonMap.put("description", pdto.getDescription());
                jsonMap.put("image", pdto.getImage());
                jsonMap.put("stock", pdto.getStock());
                jsonMap.put("status", pdto.getStatus());
                jsonMap.put("rating", pdto.getRating());
                jsonMap.put("discount", pdto.getDiscount());
                jsonMap.put("verified", pdto.isVerified());
                jsonMap.put("quantity", pdto.getQuantity());

                // Agar Seller object ka basic info bhi rakhna ho



        IndexResponse responseo = client.index(i -> i
                .index("my-index")       // name of the index
                .id("1")                 // optional: document ID
                .document(jsonMap) // the document content
        );
return responseo.id();

              // ye OpenSearch ka document ID return karega
        }
        */




   /* public int addindex(productdto pdto) throws IOException {
    try {
        Document d = new Document();

        IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
        IndexWriter writer = new IndexWriter(dir, config);

        System.out.println("wriiiiitng into index");

        d.add(new TextField("Name", pdto.getName(), Field.Store.YES));

        d.add(new StringField("Category", pdto.getCategory(), Field.Store.YES));
        d.add(new StringField("Subcategory", pdto.getSubcategory(), Field.Store.YES));
        d.add(new StringField("Description", pdto.getDescription(), Field.Store.YES));
        d.add(new StringField("Price",pdto.getPrice(), Field.Store.YES));

        writer.addDocument(d);
        writer.commit();
    }
    catch(Exception e)
    {
        System.out.println(e.getMessage());
        System.out.println(e.getStackTrace());

        return 0;
    }
    return 1;

    }*/
/*
    public void searchidx(String s) throws ParseException, IOException {
        QueryParser qp=new QueryParser("name",new StandardAnalyzer());
        Query q=qp.parse(s);
      // IndexReader rd= new IndexReader(dir);  ye use nahi kar sakte coz indexreader is abstract and ye methods ka implementation mangata hai
        IndexReader rd=DirectoryReader.open(dir);
        IndexSearcher srch=new IndexSearcher(rd);
        TopDocs tp=srch.search(q,10);
        for(int i=0;i<tp.scoreDocs.length;i++)
        {
            int id=tp.scoreDocs[i].doc;
            
        }

    }
}*/

