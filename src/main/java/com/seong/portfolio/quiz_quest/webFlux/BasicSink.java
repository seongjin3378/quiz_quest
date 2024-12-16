package com.seong.portfolio.quiz_quest.webFlux;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;



/* 멀티캐스트 Sink 생성: 여러 구독자가 동일한 데이터를 수신
Sinks.many().multicast().directAllOrNothing();
         * - 여러 구독자가 동시에 데이터를 수신하도록 설정
         * - 모든 구독자에게 방출이 성공적으로 이루어져야만 방출 완료
         * - 데이터 일관성 보장
         * - 주로 실시간 이벤트 전파에 사용
 */

/* 리플레이 Sink 생성: 구독자가 구독 시작 시 과거 모든 데이터를 수신
Sinks.many().replay().all();
         * - 구독자가 구독을 시작할 때까지 방출된 모든 데이터를 재전송
         * - 구독자가 놓친 데이터도 수신 가능
         * - 데이터 일관성 보장하지 않음
         * - 주로 과거 데이터 재전송 필요 시 사용
 */

public abstract class BasicSink<T> {
    protected final Sinks.Many<T> sink;

    protected BasicSink(Sinks.Many<T> sink) {
        this.sink = sink;
    }
    public void emit(T value) {
        sink.tryEmitNext(value);
    }
    public Flux<T> streamValues() {
        return sink.asFlux();
    }
}
