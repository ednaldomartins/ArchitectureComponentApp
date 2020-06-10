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
| <img src=https://user-images.githubusercontent.com/21205709/83977336-3c846a00-a8d6-11ea-9f66-f124ea14d5ba.jpg width="250"> | <img src=https://user-images.githubusercontent.com/21205709/83977657-92f2a800-a8d8-11ea-8c7d-519349965982.jpg width="250"> | <img src=https://user-images.githubusercontent.com/21205709/83977671-b61d5780-a8d8-11ea-9c79-2a207284d878.jpg width="250"> |

#### Buscando filmes na API através do nome do filme. Também é possível realizar buscas na lista dos filmes favoritos que estão guardados na memória do dispositivo.

<img src=https://user-images.githubusercontent.com/21205709/83977703-f8df2f80-a8d8-11ea-9c0b-ef33c46e006b.jpg width="250">    <img src=https://user-images.githubusercontent.com/21205709/83978184-2aa5c580-a8dc-11ea-887b-0ddc71b63aef.jpg width="250">

#### Ao clicar em _Ver Mais_ é possível encontrar mais filmes em uma lista imensa de acordo com a categoria selecionada.

<img src=https://user-images.githubusercontent.com/21205709/83977336-3c846a00-a8d6-11ea-9f66-f124ea14d5ba.jpg width="250">    <img src=https://user-images.githubusercontent.com/21205709/83977853-e1547680-a8d9-11ea-929c-41b4bacfac19.jpg width="250">

#### Você pode ordenar pela categoria, ou filtrar a lista de acordo com o gênero.

<img src=https://user-images.githubusercontent.com/21205709/83977892-1f519a80-a8da-11ea-8e54-4f8d9e41e1f9.jpg width="250">    <img src=https://user-images.githubusercontent.com/21205709/83977933-62ac0900-a8da-11ea-8b7b-c526ed878961.jpg width="250">

#### Cada filme contém diversas informações, como despesas, elenco, ano de lançamento, entre outras.

<img src=https://user-images.githubusercontent.com/21205709/83977954-8ff8b700-a8da-11ea-8c68-e1c847b1d42d.jpg width="250">  <img src=https://user-images.githubusercontent.com/21205709/83977957-91c27a80-a8da-11ea-9ac2-54ec984db926.jpg width="250">    <img src=https://user-images.githubusercontent.com/21205709/83977964-9dae3c80-a8da-11ea-9195-8ad9883a4866.jpg width="250">    <img src=https://user-images.githubusercontent.com/21205709/83977959-938c3e00-a8da-11ea-8a07-f1a4ca588e8e.jpg width="250">    <img src=https://user-images.githubusercontent.com/21205709/83977962-99821f00-a8da-11ea-87b5-f802458ee49e.jpg width="250">    <img src=https://user-images.githubusercontent.com/21205709/84289770-4944d000-ab19-11ea-8a25-1be682707243.jpg width="250">   

#### Os cards dos atores te direcionam para uma outra página contendo informações, como partipações do ator em outros filmes.

<img src=https://user-images.githubusercontent.com/21205709/83978108-84f25680-a8db-11ea-93b0-fe005bb87334.jpg width="250">

#### Tela de comentários dos usuários do TMDB

<img src=https://user-images.githubusercontent.com/21205709/83978069-48bef600-a8db-11ea-95e9-eb89b1a84000.jpg width="250">

### Referências

[2] [Android Developers: Guide to App Architecture](https://developer.android.com/jetpack/docs/guide)

[8] [Android Developers: Visão Geral do LiveData](https://developer.android.com/topic/libraries/architecture/livedata)

[9] [Android Developers: Salvar dados em um banco de dados local usando o Room](https://developer.android.com/training/data-storage/room/index.html)

[15] [Android Developers: Componentes da Arquitetura do Android](https://developer.android.com/topic/libraries/architecture)
