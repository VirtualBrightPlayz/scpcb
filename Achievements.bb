;achievement menu & messages by InnocentSam

;TODO: fix because this sucks
Const MAXACHIEVEMENTS=32
Dim Achievements%(MAXACHIEVEMENTS)

Const Achv008%=0, Achv012%=1, Achv035%=2, Achv049%=3, Achv055=4,  Achv079%=5, Achv096%=6, Achv106%=7, Achv148%=8, Achv205=9
Const Achv294%=10, Achv420%=11, Achv500%=12, Achv513%=13, Achv789%=14, Achv860%=15, Achv895%=16
Const Achv914%=17, Achv939%=18, Achv966%=19, Achv970=20, Achv1048=21, Achv1123=22

Const AchvMaynard%=23, AchvHarp%=24, AchvSNAV%=25, AchvOmni%=26, AchvConsole%=27, AchvTesla%=28, AchvPD%=29

Const Achv1162% = 30, Achv1499% = 31

Global UsedConsole

Global AchievementsMenu%
Dim AchievementStrings$(MAXACHIEVEMENTS)
Dim AchievementDescs$(MAXACHIEVEMENTS)
Dim AchvIMG%(MAXACHIEVEMENTS)
For i = 0 To MAXACHIEVEMENTS-1
	Local loc2% = GetINISectionLocation("Data\achievementstrings.ini", "s"+Str(i))
	AchievementStrings(i) = GetINIString2("Data\achievementstrings.ini", loc2, "string1")
	AchievementDescs(i) = GetINIString2("Data\achievementstrings.ini", loc2, "AchvDesc")
	
	Local image$ = GetINIString2("Data\achievementstrings.ini", loc2, "image") 
	
	AchvIMG(i) = LoadImage_Strict("GFX\menu\achievements\"+image+".jpg")
	AchvIMG(i) = ResizeImage2(AchvIMG(i),ImageWidth(AchvIMG(i))*userOptions\screenHeight/768.0,ImageHeight(AchvIMG(i))*userOptions\screenHeight/768.0)
Next

Global AchvLocked = LoadImage_Strict("GFX\menu\achievements\achvlocked.jpg")
AchvLocked = ResizeImage2(AchvLocked,ImageWidth(AchvLocked)*userOptions\screenHeight/768.0,ImageHeight(AchvLocked)*userOptions\screenHeight/768.0)

Function GiveAchievement(achvname%, showMessage%=True)
	If Achievements(achvname)<>True Then
		Achievements(achvname)=True
		If userOptions\achvPopup And showMessage Then
			Local loc2% = GetINISectionLocation("Data\achievementstrings.ini", "s"+achvname)
			Local AchievementName$ = GetINIString2("Data\achievementstrings.ini", loc2, "string1")
			Msg = "Achievement Unlocked - "+AchievementName
			MsgTimer=70*7
		EndIf
	EndIf
End Function

Function AchievementTooltip(achvno%)
    Local scale# = userOptions\screenHeight/768.0
    
    SetFont Font3
    Local width = StringWidth(AchievementStrings(achvno))
    SetFont Font1
    If (StringWidth(AchievementDescs(achvno))>width) Then
        width = StringWidth(AchievementDescs(achvno))
    EndIf
    width = width+20*MenuScale
    
    Local height = 38*scale
    
    Color 25,25,25
    Rect(ScaledMouseX()+(20*MenuScale),ScaledMouseY()+(20*MenuScale),width,height,True)
    Color 150,150,150
    Rect(ScaledMouseX()+(20*MenuScale),ScaledMouseY()+(20*MenuScale),width,height,False)
    SetFont Font3
    Text(ScaledMouseX()+(20*MenuScale)+(width/2),ScaledMouseY()+(35*MenuScale), AchievementStrings(achvno), True, True)
    SetFont Font1
    Text(ScaledMouseX()+(20*MenuScale)+(width/2),ScaledMouseY()+(55*MenuScale), AchievementDescs(achvno), True, True)
End Function

Function DrawAchvIMG(x%, y%, achvno%)
	Local row%
	Local scale# = userOptions\screenHeight/768.0
	Local SeparationConst2 = 76 * scale
;	If achvno >= 0 And achvno < 4 Then 
;		row = achvno
;	ElseIf achvno >= 3 And achvno <= 6 Then
;		row = achvno-3
;	ElseIf achvno >= 7 And achvno <= 10 Then
;		row = achvno-7
;	ElseIf achvno >= 11 And achvno <= 14 Then
;		row = achvno-11
;	ElseIf achvno >= 15 And achvno <= 18 Then
;		row = achvno-15
;	ElseIf achvno >= 19 And achvno <= 22 Then
;		row = achvno-19
;	ElseIf achvno >= 24 And achvno <= 26 Then
;		row = achvno-24
;	EndIf
	row = achvno Mod 4
	Color 0,0,0
	Rect((x+((row)*SeparationConst2)), y, 64*scale, 64*scale, True)
	If Achievements(achvno) = True Then
		DrawImage(AchvIMG(achvno),(x+(row*SeparationConst2)),y)
	Else
		DrawImage(AchvLocked,(x+(row*SeparationConst2)),y)
	EndIf
	Color 50,50,50
	
	Rect((x+(row*SeparationConst2)), y, 64*scale, 64*scale, False)
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D