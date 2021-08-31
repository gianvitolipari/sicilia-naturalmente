class Prodotto {
  constructor(
    idProdotto,
    categoria,
    descrizione,

    titolo,
    prezzo,
    immagineRetro,
    grano,
    minutiPrep,
    grammi,
    quantita,
    contenuto, //attr per il box
    formato,
    immagine,
  ) {
    this.idProdotto = idProdotto
    this.categoria = categoria

    this.titolo = titolo
    this.prezzo = prezzo
    this.immagineRetro = immagineRetro
    this.grano = grano
    this.descrizione = descrizione
    this.minutiPrep = minutiPrep
    this.grammi = grammi
    this.quantita = quantita
    this.contenuto = contenuto
    this.formato = formato
    this.immagine = immagine
  }
}

export default Prodotto
