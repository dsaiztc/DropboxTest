# DropboxTest
Aplicación de consulta de ebooks en la cuenta de Dropbox del usuario.

## 1 Características
### 1.1 Inicio de sesión
Al iniciar la aplicación, se requerirá al usuario que acepte los permisos necesarios sobre su cuenta de Dropbox. En este caso:

- Consulta de documentos de ebook.
- Consulta de imágenes (para obtener la portada del ebook).

Si el usuario no dispone de la aplicación de Dropbox en el terminal, o desea emplear otra cuenta diferente, se le abrirá un link en el navegador para que autorice los permisos de la aplicación en la [web de Dropbox](https://www.dropbox.com/) (iniciando sesión con la cuenta correspondiente).

### 1.2 Lista de ebooks
Aparecerá una lista con todos los ficheros disponibles en la cuenta de dropbox del usuario (aquellos ficheros con extensión *epub*).

### 1.3 Ordenar por nombre o fecha
El usuario podrá acceder a través del menú a dos opciones diferentes para ordenar la lista de archivos:

- **Ordenar por nombre**: se ordenarán los libros en función del título del libro (siembre que esté disponible en el correspondiente fichero *metadata.opf*; en caso contrario se ordenará en función del nombre del archivo).
- **Ordenar por fecha**: se ordenarán los libros en función de la fecha de modificación del archivo en la cuenta de dropbox.

![alt text](https://raw.githubusercontent.com/dsaiztc/DropboxTest/develop/ScreenShots/ss1.jpg "Ordenar por nombre")

![alt text](https://raw.githubusercontent.com/dsaiztc/DropboxTest/develop/ScreenShots/ss2.jpg "Ordenar por fecha")

![alt text](https://raw.githubusercontent.com/dsaiztc/DropboxTest/develop/ScreenShots/ss3.jpg "Menú de la aplicación")

### 1.4 Mostrar portada del libro
En caso de que el libro disponga de portada (archivo *cover.jpg*), ésta se presentará como miniatura al lado del título del libro (o del nombre del archivo si éste no está disponible).

Si el *ebook* no dispone de portada se mostrará un icono genérico.

### 1.5 Mostrar autor del libro
En caso de que el libro tenga disponible la información del autor en el fichero *metadata.opf*, se mostrará debajo del título del libro.

### 1.6 Desconexión
Si el usuario desea desvincular la aplicación de su cuenta de Dropbox, tiene a su disponibilidad un botón a tal efecto en el menú de la aplicación.

## 2 Implementación
### 2.1 API Dropbox
Para la implementación de esta aplicación se ha empleado el [API de Dropbox](https://www.dropbox.com/developers/sync) para Android. Éste proporciona las herramientas necesarias para acceder a la información del usuario en Dropbox y así poder obtener los archivos de libros electrónicos (así como los archivos de portada).

Se ha tomado como referencia el proyecto de ejemplo *Hello Dropbox Example* que viene incluido en la [librería descargada (Version 3.0.0-b2)](https://www.dropbox.com/developers/sync/sdks/android).

**NOTA:** La aplicación no guarda ningún dato de los libros, por lo que cada vez que ésta se inicie realizará la consecuente consulta a Dropbox.

**NOTA 2:** La aplicación no permite abrir ninguno de los documentos. Dicha implementación no se ha realizado ya que no era el objetivo de este desarrollo.

## 3 Consideraciones
Para las pruebas se han añadido archivos *epub* generados mediante la herramienta [Calibre](http://calibre-ebook.com/). Dicha herramienta clasifica los libros por carpetas en función del autor y el título del libro. Existirá por tanto un único archivo *epub* por carpeta, de tal forma que los archivos *cover.jpg* y *metadata.opf* que estén en dicha carpeta estarán asociados al archivo *epub* mencionado.

Esto no significa que no detecte libros que no estén bajo dicha estructura, sino que no se podrá obtener ni la imágen de portada ni los metadatos de los archivos que no lo estén.

## 4 Anexos
Se han añadido varios libros de ejemplo para mostrar la estructura comentada en el punto anterior. Dichos libros son los que se han utilizado para realizar las pruebas y son los que aparecen en las capturas de pantalla antes mostradas.
