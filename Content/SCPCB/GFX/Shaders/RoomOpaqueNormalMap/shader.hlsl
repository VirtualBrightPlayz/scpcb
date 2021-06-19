Texture2D lm0;
Texture2D lm1;
Texture2D lm2;
Texture2D diff;
Texture2D bump;
SamplerState smp;

cbuffer cbMatrices {
    matrix projectionMatrix;
    matrix viewMatrix;
    matrix modelMatrix;
}

struct VS_INPUT {
    float4 position : POSITION0;
    float3 normal : NORMAL0;
    float2 diffUv : TEXCOORD0;
    float2 lmUv : TEXCOORD1;
};

struct PS_INPUT {
    float4 position : SV_POSITION;
    float4 worldPos : POSITION0;
    float3 normal : NORMAL0;
    float2 diffUv : TEXCOORD0;
    float2 lmUv : TEXCOORD1;
};

struct PS_OUTPUT {
    float4 color : SV_Target0;
    //float4 depth : SV_Target1;
};

PS_INPUT VS(VS_INPUT input) {
    PS_INPUT output = (PS_INPUT)0;
    output.position = mul(input.position,modelMatrix);
    output.worldPos = mul(input.position,modelMatrix);
    output.position = mul(output.position,viewMatrix);
    output.position = mul(output.position,projectionMatrix);
    float4 normal = float4(input.normal.xyz,0.0);
    output.normal = mul(normal,modelMatrix).xyz;
    output.diffUv = input.diffUv;
    output.lmUv = input.lmUv;
    return output;
}

PS_OUTPUT PS(PS_INPUT input) {
    PS_OUTPUT output = (PS_OUTPUT)0;
    float4 diffColor = diff.Sample(smp,input.diffUv)*2;
    float4 lmColor0 = lm0.Sample(smp,input.lmUv);
    float4 lmColor1 = lm1.Sample(smp,input.lmUv);
    float4 lmColor2 = lm2.Sample(smp,input.lmUv);
    float3 bumpColor = bump.Sample(smp,input.diffUv).xyz-float3(0.5,0.5,0.0);
    bumpColor = normalize(float3(bumpColor.x,bumpColor.y,bumpColor.z));

    float3 lmBasis0 = float3(-1.0/sqrt(6.0),-1.0/sqrt(2.0),1/sqrt(3.0));
    float3 lmBasis1 = float3(-1.0/sqrt(6.0),1.0/sqrt(2.0),1/sqrt(3.0));
    float3 lmBasis2 = float3(sqrt(2.0/3.0),0.0,1/sqrt(3.0));

    float4 lmColor = lmColor0 * dot(lmBasis0,bumpColor) + lmColor1 * dot(lmBasis1,bumpColor) + lmColor2 * dot(lmBasis2,bumpColor);
    lmColor = float4(lmColor.x, lmColor.y, lmColor.z, 1.0);

    output.color = diffColor*lmColor;
    //output.depth = float4(input.position.w,0.0,0.0,1.0);
    return output;
}
