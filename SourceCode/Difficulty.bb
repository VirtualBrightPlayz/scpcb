;TODO: Rewrite this entire file.
Type Difficulty
	Field name$
	Field description$
	Field permaDeath%
	Field aggressiveNPCs%
	Field saveType%
	Field otherFactors%

	Field customizable%
End Type

Global difficulties.Difficulty[4]

Global SelectedDifficulty.Difficulty

Const SAFE%=0, EUCLID%=1, KETER%=2, CUSTOM%=3

Const SAVEANYWHERE% = 0, SAVEONQUIT%=1, SAVEONSCREENS%=2

Const EASY% = 0, NORMAL% = 1, HARD% = 2

difficulties[SAFE] = New Difficulty
difficulties[SAFE]\name = "Safe"
difficulties[SAFE]\description ="The game can be saved any time. However, as in the case of SCP Objects, a Safe classification does not mean that handling it does not pose a threat."
difficulties[SAFE]\permaDeath = False
difficulties[SAFE]\aggressiveNPCs = False
difficulties[SAFE]\saveType = SAVEANYWHERE
difficulties[SAFE]\otherFactors = EASY

difficulties[EUCLID] = New Difficulty
difficulties[EUCLID]\name = "Euclid"
difficulties[EUCLID]\description = "Euclid-class objects are insufficiently understood or inherently unpredictable, "
difficulties[EUCLID]\description = difficulties[EUCLID]\description +"such that reliable containment is not always possible. "
difficulties[EUCLID]\description = difficulties[EUCLID]\description +"In Euclid difficulty, saving only allowed at specific locations marked by lit up computer screens."
difficulties[EUCLID]\permaDeath = False
difficulties[EUCLID]\aggressiveNPCs = False
difficulties[EUCLID]\saveType = SAVEONSCREENS
difficulties[EUCLID]\otherFactors = NORMAL

difficulties[KETER] = New Difficulty
difficulties[KETER]\name = "Keter"
difficulties[KETER]\description = "Keter-class objects are considered the most dangerous ones in Foundation containment. "
difficulties[KETER]\description = difficulties[KETER]\description +"The same can be said for this difficulty level: the SCPs are more aggressive, and you have only one life - when you die, the game is over. "
difficulties[KETER]\permaDeath = True
difficulties[KETER]\aggressiveNPCs = True
difficulties[KETER]\saveType = SAVEONQUIT
difficulties[KETER]\otherFactors = HARD

difficulties[CUSTOM] = New Difficulty
difficulties[CUSTOM]\name = "Custom"
difficulties[CUSTOM]\permaDeath = False
difficulties[CUSTOM]\aggressiveNPCs = True
difficulties[CUSTOM]\saveType = SAVEANYWHERE
difficulties[CUSTOM]\customizable = True
difficulties[CUSTOM]\otherFactors = EASY

SelectedDifficulty = difficulties[SAFE]
;~IDEal Editor Parameters:
;~C#Blitz3D