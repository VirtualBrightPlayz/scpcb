Texture2D diff;
SamplerState smp; 

cbuffer cbVertex {
    matrix viewMatrix;
    matrix projectionMatrix;
    matrix modelMatrix;
}

cbuffer cbFragment {
    float4 inColor;
}

struct VS_INPUT {
    float4 position : POSITION0;
    float3 normal : NORMAL0;
    float2 uv : TEXCOORD0;
};

struct PS_INPUT {
    float4 position : SV_POSITION;
    float3 normal : NORMAL0;
    float2 uv : TEXCOORD0;
};

struct PS_OUTPUT {
    float4 color : SV_Target0;
    float4 depth : SV_Target1;
};

PS_INPUT VS(VS_INPUT input) {
    PS_INPUT output = (PS_INPUT)0;
    output.position = mul(input.position,modelMatrix);
    output.position = mul(output.position,viewMatrix);
    output.position = mul(output.position,projectionMatrix);
    float4 normal = float4(input.normal.xyz,0.0);
    output.normal = normalize(mul(normal,modelMatrix).xyz);
    output.uv = input.uv;
    return output;
}

PS_OUTPUT PS(PS_INPUT input) {
    PS_OUTPUT output = (PS_OUTPUT)0;
    float4 diffColor = diff.Sample(smp,input.uv);

    diffColor = diffColor*inColor;

    output.color = diffColor;
    output.depth = float4(input.position.w,0.0,0.0,1.0);
    return output;
}
