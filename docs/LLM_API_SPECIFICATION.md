# LLM 통신 명세서

## 개요
Finance Service에서 LLM 담당자에게 투자성향 정보를 전달하고, 포트폴리오 추천 결과를 받아오는 API 통신 명세서입니다.

## API 엔드포인트
- **URL**: `POST {LLM_API_URL}/api/llm/recommend`
- **Content-Type**: `application/json`
- **Authorization**: `Bearer {LLM_API_KEY}`

## 요청 형식 (Request)

### HTTP 헤더
```
Content-Type: application/json
Authorization: Bearer {LLM_API_KEY}
X-Request-Source: finance-service
X-Retry-Attempt: 1
```

### 요청 본문 (JSON)
```json
{
  "userId": 1,
  "userName": "김철수",
  "seedMoney": 10000000.00,
  "currentBalance": 8500000.00,
  "investmentProfile": {
    "profileType": "BALANCED",
    "profileDescription": "균형",
    "availableAssets": 5000000,
    "investmentGoal": "RETIREMENT",
    "goalDescription": "노후"
  },
  "interestedSectors": ["기술", "금융", "바이오"],
  "marketData": {
    "majorStocks": [ /* Stock 목록 객체 배열 (간단 정보) */ ],
    "sectors": [ /* Sector 목록 객체 배열 (간단 정보) */ ]
  },
  "requestId": "req_1703123456789",
  "timestamp": 1703123456789
}
```

### 요청 필드 설명
| 필드 | 타입 | 설명 |
|------|------|------|
| userId | Long | 사용자 ID |
| userName | String | 사용자 이름 |
| seedMoney | BigDecimal | 초기 시드머니 |
| currentBalance | BigDecimal | 현재 잔고 |
| investmentProfile | Object | 투자성향 정보 |
| investmentProfile.profileType | String | 투자성향 타입 (HIGH_RISK, BALANCED, CONSERVATIVE) |
| investmentProfile.profileDescription | String | 투자성향 설명 |
| investmentProfile.availableAssets | BigDecimal | 투자가능자산 |
| investmentProfile.investmentGoal | String | 투자목표 (RETIREMENT, EDUCATION, HOUSING, EMERGENCY, GROWTH) |
| investmentProfile.goalDescription | String | 투자목표 설명 |
| interestedSectors | Array | 관심섹터 목록 |
| marketData | Object | 시장 데이터 |
| marketData.majorStocks | Array | 주요 종목 목록 (간단 정보) |
| marketData.sectors | Array | 섹터 목록 (간단 정보) |
| requestId | String | 요청 ID (고유 식별자) |
| timestamp | Long | 요청 시간 (Unix timestamp) |

## 응답 형식 (Response)

### 성공 응답 (200 OK)
```json
{
  "portfolioName": "AI 추천 포트폴리오",
  "allocationStocks": 70.00,
  "allocationSavings": 30.00,
  "expected1YrReturn": 8.50,
  "recommendedStocks": [
    {
      "stockId": "005930",
      "allocationPct": 25.00
    },
    {
      "stockId": "000660",
      "allocationPct": 20.00
    },
    {
      "stockId": "035420",
      "allocationPct": 15.00
    },
    {
      "stockId": "207940",
      "allocationPct": 10.00
    }
  ],
  "reasoning": "균형형 투자성향에 맞춰 안정성과 수익성을 고려한 포트폴리오를 구성했습니다.",
  "riskLevel": "MEDIUM",
  "confidence": 0.85
}
```

### 응답 필드 설명
| 필드 | 타입 | 설명 |
|------|------|------|
| portfolioName | String | 포트폴리오 이름 |
| allocationStocks | BigDecimal | 주식 배분 비율 (%) |
| allocationSavings | BigDecimal | 예금 배분 비율 (%) |
| expected1YrReturn | BigDecimal | 예상 1년 수익률 (%) |
| recommendedStocks | Array | 추천 종목 목록 |
| recommendedStocks[].stockId | String | 종목 코드 |
| recommendedStocks[].allocationPct | BigDecimal | 포트폴리오 내 배분 비율 (%) |
| reasoning | String | 추천 이유 (선택적) |
| riskLevel | String | 위험도 (LOW, MEDIUM, HIGH) (선택적) |
| confidence | Double | 신뢰도 (0.0 ~ 1.0) (선택적) |

### 오류 응답 (4xx, 5xx)
```json
{
  "error": "INVALID_REQUEST",
  "message": "요청 데이터가 올바르지 않습니다.",
  "details": "investmentProfile.profileType 필드가 누락되었습니다."
}
```

## 재시도 정책
- **최대 재시도 횟수**: 3회
- **재시도 간격**: 1초
- **타임아웃**: 30초

## 환경 변수 설정 예시
```
LLM_API_URL=http://localhost:8081/api/llm/recommend
LLM_API_KEY=your-api-key-here
```

