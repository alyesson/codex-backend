package br.com.codex.v1.service;

import br.com.codex.v1.utilitario.NcmData;
import br.com.codex.v1.utilitario.NcmList;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class NcmFetcher {
    private static final String CACHE_FILE = "ncm.json";
    private static final String NCM_URL = "https://portalunico.siscomex.gov.br/classif/api/publico/nomenclatura/download/json?perfil=PUBLICO";
    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final int CACHE_EXPIRATION_DAYS = 7;

    private Map<String, NcmData> ncmIndex;
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public NcmFetcher() {
        this.httpClient = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true).build();
        this.objectMapper = new ObjectMapper();
        this.loadData();
    }

    public String downloadJson() throws IOException {
        Request request = new Request.Builder()
                .url(NCM_URL).header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Erro na requisição: " + response.code());
            }
            return response.body().string();
        }
    }

    public void saveJson(String jsonData) throws IOException {
        Files.writeString(Path.of(CACHE_FILE),jsonData,StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING);
    }

    private boolean isCacheValid() {
        File cacheFile = new File(CACHE_FILE);
        if (!cacheFile.exists()) {
            return false;
        }

        long lastModified = cacheFile.lastModified();
        long expirationTime = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(CACHE_EXPIRATION_DAYS);

        return lastModified > expirationTime;
    }

    private void loadData() {
        try {
            String jsonData;

            if (isCacheValid()) {
                try {
                    jsonData = Files.readString(Path.of(CACHE_FILE));
                } catch (IOException e) {
                    jsonData = downloadJson();
                    saveJson(jsonData);
                }
            } else {
                jsonData = downloadJson();
                saveJson(jsonData);
            }

            buildNcmIndex(jsonData);
        } catch (IOException | ParseException e) {
            throw new RuntimeException("Erro ao carregar dados NCM", e);
        }
    }

    private void buildNcmIndex(String jsonData) throws IOException, ParseException {
        Map<String, Object> jsonMap = objectMapper.readValue(jsonData, Map.class);
        List<Map<String, Object>> nomenclaturas = (List<Map<String, Object>>) jsonMap.get("Nomenclaturas");

        this.ncmIndex = new HashMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

        for (Map<String, Object> item : nomenclaturas) {
            String codigoNcm = item.get("Codigo").toString().replace(".", "");

            NcmData ncmData = new NcmData();
            ncmData.setCodigoNcm(codigoNcm);
            ncmData.setDescricaoNcm(item.get("Descricao").toString());
            ncmData.setDataInicio(dateFormat.parse(item.get("Data_Inicio").toString()));
            ncmData.setDataFim(dateFormat.parse(item.get("Data_Fim").toString()));
            ncmData.setTipoAto(item.get("Tipo_Ato_Ini").toString());
            ncmData.setNumeroAto(item.get("Numero_Ato_Ini").toString());
            ncmData.setAnoAto(Integer.parseInt(item.get("Ano_Ato_Ini").toString()));

            ncmIndex.put(codigoNcm, ncmData);
        }
    }

    public NcmList getAll(boolean onlyNcm8Digits) {
        List<NcmData> listNcm = new ArrayList<>();

        for (Map.Entry<String, NcmData> entry : ncmIndex.entrySet()) {
            if (!onlyNcm8Digits || entry.getKey().length() == 8) {
                listNcm.add(entry.getValue());
            }
        }
        return new NcmList(listNcm);
    }

    public NcmData getCodigoNcm(String codigoNcm) {
        return ncmIndex.getOrDefault(codigoNcm, createEmptyNcm());
    }

    private NcmData createEmptyNcm() {
        NcmData empty = new NcmData();
        empty.setCodigoNcm("");
        empty.setDescricaoNcm("");
        empty.setDataInicio(new Date());
        empty.setDataFim(new Date());
        empty.setTipoAto("");
        empty.setNumeroAto("");
        empty.setAnoAto(0);
        return empty;
    }

    public boolean refreshData() {
        try {
            String jsonData = downloadJson();
            saveJson(jsonData);
            buildNcmIndex(jsonData);
            return true;
        } catch (IOException | ParseException e) {
            return false;
        }
    }
}