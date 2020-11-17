package Util;


import UnoProvaServerVecchioo.createCommand;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.LinkedTreeMap;

public class JsonUtil {




    /**
     * metodo che, data una string json, restituisce una classe annidata al suo interno
     * @param obj oggetto contenete la classe
     * @param clazz la classe da estrarre
     * @param gson
     * @param <A>
     * @return oggetto della classe 'clazz'
     */
    public static <A> A nestedClassFromJson(Object obj, Class<A> clazz, Gson gson)  {
        return gson.fromJson(new Gson().toJson(((LinkedTreeMap<String, Object>) obj)), clazz);
    }

    /**
     * Metodo che estrae dall'oggetto CrateCommand del client, la stringa
     * mappata sull'aatributo 'comando' per poter interpretare
     * quale comando è stato inviato dal client
     * @param json il json ricevuto dal client
     * @return la string corrispondente al comando
     */
    public static String getComandoDaJson(String json)
    {
        JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
        return obj.get("comando").getAsString();
    }

    /**
     * Metodo che crea un oggetto della classe {@link UnoProvaServerVecchioo.createCommand} sottoforma
     * di stringa Json
     * @param comando
     * @param obj
     * @return
     */
    public static String setComandoJson(String comando, Object obj)
    {
        createCommand cc = new createCommand(comando, obj);
        return new Gson().toJson(cc);
    }

   /* public static String getJsonComando(String comando, Object obj)
    {
        CreateCommand cc = new CreateCommand(comando, obj);
        return new Gson().toJson(cc);
    }*/





/*    public static JsonNode toJson(Object a) {
        return objectMapper.valueToTree(a);
    }

    public static String stingify(JsonNode node) throws JsonProcessingException {
        return generateString(node, false);
    }

    public static String prettyPrint(JsonNode node) throws JsonProcessingException {
        return generateString(node, true);
    }

    private static String generateString(JsonNode node, boolean pretty) throws JsonProcessingException {
        ObjectWriter objectWriter = objectMapper.writer();
        if ( pretty )
            objectWriter = objectWriter.with(SerializationFeature.INDENT_OUTPUT);
        return objectWriter.writeValueAsString(node);
    }*/
}
