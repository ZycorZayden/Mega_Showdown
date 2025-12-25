#version 150

uniform sampler2D Sampler0;
uniform vec4 TeraTint;

in vec2 vUv;
in vec3 vNormal;
in float vDepth;

out vec4 FragColor;

void main() {
    vec4 baseTex = texture(Sampler0, vUv);

    /* CUTOUT RULE */
    if (baseTex.a < 0.5) {
        discard;
    }

    vec3 n = normalize(vNormal);

    /* Depth-driven refraction */
    vec2 refractDir = normalize(n.xy + 0.0001);
    float depthScale = clamp(vDepth * 0.15, 0.6, 1.6);

    vec3 layer1 = texture(Sampler0, vUv + refractDir * 0.020 * depthScale).rgb;
    vec3 layer2 = texture(Sampler0, vUv + refractDir * 0.045 * depthScale).rgb;
    vec3 layer3 = texture(Sampler0, vUv - refractDir * 0.030 * depthScale).rgb;

    /* Faceted lighting */
    vec3 lightDir = normalize(vec3(0.4, 0.85, 0.45));
    float facet = abs(dot(n, lightDir));
    facet = pow(facet, 3.5);

    /* Edge emphasis */
    float edge = pow(1.0 - abs(dot(n, vec3(0, 0, 1))), 2.2);

    /* Monochrome crystal depth */
    vec3 crystal =
        baseTex.rgb * 0.40 +
        layer1 * 0.30 +
        layer2 * 0.20 +
        layer3 * 0.10;

    /* Lighting */
    crystal *= (0.8 + facet * 1.0);

    /* Edge glow (same color only) */
    crystal += edge * TeraTint.rgb * 0.35;

    /* Apply tera tint (NO dispersion) */
    crystal *= mix(vec3(1.0), TeraTint.rgb, 0.9);

    FragColor = vec4(crystal, 1.0);
}
