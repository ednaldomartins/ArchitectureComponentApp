# ArchitectureComponentApp
Repositório para criação de um app usando componentes de arquitetura Android. Esse repositório é a base inicial para o aplicativo Libflix que está em um repositório privado.

### Componentes de Arquitetura Android


Abaixo serão apresentados alguns *__Componentes de Arquitetura Android__* que foram anunciados na Google I/O 2017, os quais, são capazes de tornar a arquitetura dos aplicativos componentizada. Para demonstrar a capacidade desses componentes, o aplicativo Libflix foi desenvolvido. Além disso, técnicas de Micro Frontends foram usadas, com o objetivo de construir uma aplicação Android, componentizada, escalável e testável.

Esses componentes podem trazer diversos benefícios. As principaiscaracterísticas desses componentes são [2]:

* **LifeCycle**: Gerencia o ciclo de vida do aplicativo, auxiliando os componentes Activity e Fragment a manterem as suas configurações salvas através da ViewModel, sendo assim, evitando perda de dados, e tornando o armazenamento ou o carregamento dessas informações mais seguro [15].

* **LiveData**: Deve ser usado para criar objetos que podem notificar seus observadores quando eles são alterados, ou quando há alterações feitas no banco de dados [15]. Esse componente trabalha em conjunto com o LifeCycle, reconhecendo o ciclo de vida da aplicação [8].

* **ViewModel**: É responsável por armazenar os dados presentes na interface de usuário, que não são descartados quando um aplicativo é rotacionado, ou para guardar dados que não estejam relacionados às configurações da Activity e Fragment [15].

* **Room**: Guarda os dados das aplicações em execução em cache no armazenamento do dispositivo [9]. Essa ferramenta faz um mapeamento de objetos SQLite e pode ser utilizado para retornar um Objeto do tipo LiveData [15].

### Arquitetura do Aplicativo

![image](https://user-images.githubusercontent.com/21205709/83975004-99c4ef00-a8c7-11ea-83ea-40a5262712f0.png)


### Telas do Aplicativo

#### Splash Screen

<img src=https://user-images.githubusercontent.com/21205709/84289783-4ba72a00-ab19-11ea-84b3-5ac35465984e.jpg width="250">

| Página Inicial | Página de Busca na API | Página de Favoritos |
|----------------|------------------------|---------------------|
| <img src=https://user-images.githubusercontent.com/21205709/92007795-c351b300-ed1c-11ea-9c25-dcdf85d1b0b0.png width="250"> | <img src=https://user-images.githubusercontent.com/21205709/92010253-f9446680-ed1f-11ea-9c80-947c8579f278.png width="250"> | <img src=https://user-images.githubusercontent.com/21205709/92008108-188dc480-ed1d-11ea-841d-5735eb8dccbe.png width="250"> |

#### Buscando filmes na API através do nome do filme. Também é possível realizar buscas na lista dos filmes favoritos que estão guardados na memória do dispositivo.

<img src=https://user-images.githubusercontent.com/21205709/92008210-3f4bfb00-ed1d-11ea-9e38-f92a98ec0fcd.png width="250">    <img src=https://user-images.githubusercontent.com/21205709/92010540-49bbc400-ed20-11ea-8e82-19f593f7ad5c.png width="250">

#### Ao clicar em _Ver Mais_ é possível encontrar mais filmes em uma lista imensa de acordo com a categoria selecionada.

<img src=https://user-images.githubusercontent.com/21205709/92007795-c351b300-ed1c-11ea-9c25-dcdf85d1b0b0.png width="250">    <img src=https://user-images.githubusercontent.com/21205709/92010716-7ff94380-ed20-11ea-850c-30111b6707b0.png width="250">

#### Você pode ordenar pela categoria, ou filtrar a lista de acordo com o gênero.

<img src=https://user-images.githubusercontent.com/21205709/92010835-a919d400-ed20-11ea-848b-fc82991bf4af.png width="250">    <img src=https://user-images.githubusercontent.com/21205709/92010840-aa4b0100-ed20-11ea-9293-5513e26e8b4c.png width="250">

#### Cada filme contém diversas informações, como despesas, elenco, ano de lançamento, entre outras.

<img src=https://user-images.githubusercontent.com/21205709/92011132-04e45d00-ed21-11ea-912f-57e563dc6059.png width="250">  <img src=https://user-images.githubusercontent.com/21205709/92011138-06158a00-ed21-11ea-9f35-a1a78182719f.png width="250">    <img src=https://user-images.githubusercontent.com/21205709/92011140-07df4d80-ed21-11ea-99a5-856fe9c90246.png width="250">    <img src=https://user-images.githubusercontent.com/21205709/92011150-0ada3e00-ed21-11ea-9e43-9e4696f0d778.png width="250">      

#### Os cards dos atores te direcionam para uma outra página contendo informações, como partipações do ator em outros filmes.

<img src=https://user-images.githubusercontent.com/21205709/92011735-e337a580-ed21-11ea-82c1-96a86f598005.png width="250">

#### Tela de comentários dos usuários do TMDB

<img src=https://user-images.githubusercontent.com/21205709/92011155-0ca40180-ed21-11ea-9723-e88868053fd6.png width="250">  
<img src=https://user-images.githubusercontent.com/21205709/92011435-6f959880-ed21-11ea-94fd-6d8f1a3ebb9c.png width="250">

#### Tela do Usuário e configurações

<img src=https://user-images.githubusercontent.com/21205709/92011857-0eba9000-ed22-11ea-90ab-11fa183dea02.png width="250">  <img src=https://user-images.githubusercontent.com/21205709/92011860-0febbd00-ed22-11ea-8520-2573b3292870.png width="250">

### Referências

[2] [Android Developers: Guide to App Architecture](https://developer.android.com/jetpack/docs/guide)

[8] [Android Developers: Visão Geral do LiveData](https://developer.android.com/topic/libraries/architecture/livedata)

[9] [Android Developers: Salvar dados em um banco de dados local usando o Room](https://developer.android.com/training/data-storage/room/index.html)

[15] [Android Developers: Componentes da Arquitetura do Android](https://developer.android.com/topic/libraries/architecture)
