# Task 1: Car constructor

## Purpose

Get familiar with channels and how to organize them to do work in parallel.

## Introduction

Let’s assume that you have a small automated workshop that produces cars on the automation line. But it is not full
automated, and it should be observed by some car constructor. Each machine uses **body line** and **equipment line** to
construct a vehicle with car factory. And there is **order desk**, which collects the orders and starts the whole
process.

## Task description

Your task is make possible to produce cars with **two** factories in parallel. So, our program should:

* Take an order from singe order list
* Pick Constructor _(in parallel)_
* Create body _(in parallel)_
* Create Equipment _(in parallel)_
* Combine the body and equipment
* Provide a car

# Task 2: Search engine

## Purpose

Demonstrate base approaches to work with **kotlin flows**. Get familiar with some flow's operators and how to use them
in coroutine context.

## Introduction

Just imagine, that _EPAM_ has its own online cinema. It provides access to a lot of video content and other data, which
are related to this content. **Asset** - is the base abstract entity, which represents our content. And it has three **Type**'s -
_VOD_ (video on demand), _LIVE_ (streams) and _CREW_ (people, who participated in film making process). So,
for example, user can find a film, see small description, take a look on cast and crew and so on. This is just a concept
in order to help you understand what we a talking about.

### Task description

Your task is implement search engine, which should work with user's input and understand how and where content should be
searched. Engine MUST be able to:

* parse user's input and transform it to the search [Query]
* search mechanism should ignore a letter's case
* by default, matches should be found with **contains** method.
* recognize special character in the text and modifies search approach:
    * **?** - indicate the type of content the user is looking for. Could be placed ONLY at the end of the text. For
      example: _Thor 3?VOD_.
    * **@** - changes the approach to finding matches to **startWith**. Could be placed ONLY on the beginning of the
      text For example:
      assets -> [Al Pacino, Pacman], query -> _pac_. Only [Pacman] should be found.
* depending on the request, the mechanism must search among all content or according to the type specified in the
  request 
* program should work and make requests to the [SearchEngine] until user types "exit" (in any letter case)


