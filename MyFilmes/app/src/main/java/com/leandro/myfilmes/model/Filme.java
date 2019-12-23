package com.leandro.myfilmes.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class Filme implements Serializable, Parcelable {


    //Serializabe = pode ser passado como parâmetro putExtra para chamar uma activity
    //Parcelable = pode ser passada uma LISTA da objetos da classe como uma parâmetro putExtra para chamar uma activity

    //O nome das variáveis deve ser idêntico ao dos títulos das colunas do Google Sheet
    private String Title;
    private String Year;
    private String imdbID;
    private String Type;
    private String Poster;
    private String Runtime;
    private String Released;
    private String Rated;
    private String Genre;
    private String Director;
    private String Writer;
    private String Actors;
    private String Plot;
    private ArrayList<Rating> Ratings;

    public Filme()
    {
    }

    //region MÉTODOS OBRIGATÓRIOS DE PARCELABLE

    protected Filme(Parcel in) {
        Title = in.readString();
        Year = in.readString();
        imdbID = in.readString();
        Type = in.readString();
        Poster = in.readString();
    }

    public static final Creator<Filme> CREATOR = new Creator<Filme>()
    {
        @Override
        public Filme createFromParcel(Parcel source) {
            return new Filme(source);
        }

        @Override
        public Filme[] newArray(int size) {
            return new Filme[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Title);
        dest.writeString(Year);
        dest.writeString(imdbID);
        dest.writeString(Type);
        dest.writeString(Poster);

    }

    public String getTitle() {
        return Title;
    }

    public String getActors() {
        return Actors;
    }

    public void setActors(String actors) {
        Actors = actors;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        this.Year = year;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        this.Type = type;
    }

    public String getPoster() {
        return Poster;
    }

    public void setPoster(String poster) {
        this.Poster = poster;
    }

    public String getReleased() {
        return Released;
    }

    public void setReleased(String released) {
        Released = released;
    }

    public String getRated() {
        return Rated;
    }

    public void setRated(String rated) {
        Rated = rated;
    }

    public String getGenre() {
        return Genre;
    }

    public void setGenre(String genre) {
        Genre = genre;
    }

    public String getDirector() {
        return Director;
    }

    public void setDirector(String director) {
        Director = director;
    }

    public String getWriter() {
        return Writer;
    }

    public void setWriter(String writer) {
        Writer = writer;
    }

    public String getRuntime() {
        return Runtime;
    }

    public void setRuntime(String runtime) {
        Runtime = runtime;
    }

    public String getPlot() {
        return Plot;
    }

    public void setPlot(String plot) {
        Plot = plot;
    }

    public ArrayList<Rating> getRatings() {
        return Ratings;
    }

    public void setRatings(ArrayList<Rating> ratings) {
        Ratings = ratings;
    }

    public class Rating {
        private String Source;
        private String Value;

        public String getSource() {
            return Source;
        }

        public void setSource(String source) {
            this.Source = source;
        }

        public String getValue() {
            return Value;
        }

        public void setValue(String value) {
            this.Value = value;
        }
    }
}
