# Functional Draft
***
## Summary
I'll try to create some kind of Roleplay platform using LLMs, but instead of just relying on current character models (and scenary models), more in depth world descriptions will be created and handled, in order to assist the LLMs to create a more easy to understand and easy to follow world and Roleplay experience.

Every interaction won't be just one big message to the Text-gen LLM, but instead, it'll be split in chunks for the different concepts being handled here. This **should** improve consistency in long sessions (new or loaded sessions).

***
## TODO:
- [ ] Describe current Scene
- [ ] How will be modeles the Activity

***

## Static concepts

### Location
The idea here is to be able to create **Locations**, which can be hierarchically linked (sub-location)
These **Locations** will have 
- Physical description: how the place looks like, what does it have/contain
- Social description: if there is any civilization inhabiting the Location, including their political order, beliefs or rules affecting this.
- Activity: What's _happening_ in the **location** in this moment. (This requires more thought and refinement).
- Roles:
    - **Actors**, in the locations, will have a defined Role. This roles can have a description regarding how the avatars fit in the Location.

### Actor
An Actor is a functional being, with their own mind. If these Actors are controlled by a **User**, then they'll be **Avatars**.
If they're controlled by the system, they become **Characters**.
**Actors** have:
- Physical description: how these beings look like.
- **Outfits**: In case the do use outfits, a set of **outfits** that the **Actors** might use. Also, in which _situations_ they might use these outfits. Actors should be able to change their outfits. This seems to require some refinement.
#### Character
A _system_ controlled **Actor**

They should have a defined **behavior**
The **behavior** consists of:
- Personality: Describes how the **Character** behaves. Reflects its mind.
- Memory: Past things that the **Character** remembers. Includes interactions with other **Actors**
#### Avatar
A **user** controlled **Actor**
**Users** could control many **Avatars** in a **Scene**. There can be many **Users** at the same **Scene** (Multiplayer).

***

## Chat session - Scene

### Scene
### Scenario

***
## Use Cases
### User
#### Create Avatar

## Random Thoughts

### What happens when the user wants to send a message.
When the user wants to send a message, using an Avatar, we need to include:
- Current Location descriptions. (all of them)
- Active Roles (**Active** has to be defined and refined)
    - Actor Physical Description
    - Actor Outfits (One will be marked as Current)
    - **Characters**:
        - Personality
        - Memory    
    - Current Avatar:
        - Actor P. Description
        - Current Actor Outfit
        - **Avatar Event text**
        - **Avatar speech text**

#### Event
This is something happening at a given time. Describes what's happening and what the **Avatar** is doing, and non-avatar events. It could be possible to include other Actor actions. **It includes Actor thoughts (for now, but quite possible I remove this).**




