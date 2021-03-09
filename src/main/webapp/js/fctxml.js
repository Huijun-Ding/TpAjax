/**
 * Cette méthode "Ajax" affiche le XML.
 *
 * On utilise la propriété 'responseText' de l'objet XMLHttpRequest afin
 * de récupérer sous forme de texte le flux envoyé par le serveur.
 */
function afficheXML ()
	{
	// Objet XMLHttpRequest.
	var xhr = new XMLHttpRequest();

	// Requête au serveur avec les paramètres éventuels.
	xhr.open("GET","ServletAuteur");

	// On précise ce que l'on va faire quand on aura reçu la réponse du serveur.
	xhr.onload = function()
		{
		// Si la requête http s'est bien passée.
		if (xhr.status === 200)
			{
			// Elément html que l'on va mettre à jour.
			var elt = document.getElementById("tt_zone");
			elt.innerHTML = xhr.responseText;
			}
		};
	
	// Envoie de la requête.
	xhr.send();
	}


/**
 * Cette méthode "Ajax" affiche la liste des auteurs.
 *
 * Utilise la propriété 'responseXML' de l'objet XMLHttpRequest afin
 * de récupérer sous forme d'arbre DOM le document XML envoyé par le serveur.
 */
function l_auteurs ()
	{
            var xhr = new XMLHttpRequest();
    
            xhr.open("GET","ServletAuteur");
            
            xhr.onload = function()
		{
		if (xhr.status === 200)
                    {
                        elt = document.getElementById("lnom"); 
                        elt.innerHTML = "<option>----</option>";
                        var i, suggestions;
                        suggestions = xhr.responseXML.getElementsByTagName("nom"); 
                        console.log(suggestions);
			for(i = 0; i < suggestions.length; i++) { 
                            nom = suggestions[i].firstChild.nodeValue;
                            console.log(nom);
                            elt = document.getElementById("lnom"); 
                            elt.insertAdjacentHTML('beforeend','<option>' + nom + '</option>'); // "afterbegin" dans l'ordre inverse
                        }
                        document.getElementById("bt_auteurs").disabled= 'disabled';
                    }
		};
            xhr.send();
	}


/**
 * Cette méthode "Ajax" affiche la liste des citations.
 *
 * Utilise la propriété 'responseXML' de l'objet XMLHttpRequest afin
 * de récupérer sous forme d'arbre DOM le document XML envoyé par le serveur.
 */
function l_citations()
{
    var xhr = new XMLHttpRequest();

    //xhr.open("GET", "ServletCitation");
    var myselect = document.getElementById("lnom").value;
    xhr.open("GET", "ServletCitation?nomauteur=" + myselect);

    xhr.onload = function ()
    {
        if (xhr.status === 200)
        {
            suggestions = xhr.responseXML.getElementsByTagName("citation");
            elt = document.getElementById("lcitations");
            elt.innerHTML = "";
            elt.insertAdjacentHTML("beforeend","<ul>");

            for (i = 0; i < suggestions.length; i++) {
                citation = suggestions[i].firstChild.nodeValue;
                elt.insertAdjacentHTML("beforeend", "<li>" + citation + "</li>"); // "afterbegin" dans l'ordre inverse
            }
        elt.insertAdjacentHTML('beforeend',"</ul>");
        }
    };
    xhr.send();
}


/**
 * Cette méthode "Ajax" simule la zone de recherche 'Google'.
 */
function processKey ()
	{
	// Objet XMLHttpRequest.
	var xhr = new XMLHttpRequest();

    var myinput = document.getElementById("saisie").value;
    xhr.open("GET", "ServletGoogle?mot_begin=" + myinput);

    xhr.onload = function ()
    {
        if (xhr.status === 200)
        {
            suggestions = xhr.responseXML.getElementsByTagName("mot");
           
            elt = document.getElementById("zoneaff");
            if(suggestions!=null){
            elt.style.display = "block";
            elt.innerHTML = "";
            for (i = 0; i < suggestions.length; i++) {
                m = suggestions[i].firstChild.nodeValue;
                elt.insertAdjacentHTML("beforeend", "<p>" + m + "</p>"); // "afterbegin" dans l'ordre inverse
            }
        }}
    else{
         elt.style.display = "none";
    }
    };
	// Envoie de la requête.
	xhr.send();
        
	}


/**
 * Cette méthode "Ajax" permet de tester les paramètres passés par l'url.
 */
function testEncodeUrl ()
	{
	// Objet XMLHttpRequest.
	var xhr = new XMLHttpRequest();

	// Requête au serveur avec les paramètres éventuels.
	var param = "texte=" + encodeURIComponent(document.getElementById("envoie").value);
	var url = "ServletEncode";
	alert(url + "?" + param);

	xhr.open("POST",url,true);
	xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

	// On précise ce que l'on va faire quand on aura reçu la réponse du serveur.
	xhr.onload = function()
		{
		// Si la requête http s'est bien passée.
		if (xhr.status === 200)
			// Elément html que l'on va mettre à jour.
			document.getElementById("recue").value = xhr.responseXML.getElementsByTagName("msg")[0].firstChild.nodeValue ;
		};

	// Envoie de la requête.
	xhr.send(param);
	}


/**
 * Lancement après le chargement du DOM.
 */
document.addEventListener("DOMContentLoaded", () => {

	document.getElementById("bt_zone").addEventListener("click",afficheXML);
	document.getElementById("bt_Url").addEventListener("click",testEncodeUrl);
        document.getElementById("bt_auteurs").addEventListener("click",l_auteurs);
        document.getElementById("lnom").addEventListener("change",l_citations);
        document.getElementById("saisie").addEventListener("keyup",processKey);
});