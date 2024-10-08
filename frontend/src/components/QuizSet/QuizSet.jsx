import styles from './QuizSet.module.css';
import { useCallback, useEffect, useRef, useState } from 'react';
import Quiz from '../Quiz/Quiz';
import LoadingIndicator from '../LoadingIndicator.jsx/LoadingIndicator';
import instance from '../../utils/axios/instance';
import { API_URL } from '../../constants';
import { useStudentQuizsetDetail } from '../../hooks/api/useStudentQuizsetDetail';
import Countdown from '../Countdown/Countdown';

export default function QuizSet({ quizSetId, reportSetId, finish }) {
  const [step, setStep] = useState(null);
  const { data } = useStudentQuizsetDetail(quizSetId);
  const quizSetData = data?.data;
  const quizList = quizSetData.quizzes;
  const answers = useRef(Array(quizList.length).fill(null));
  const interval = useRef(null);
  const submit = useCallback(
    (data) => {
      const requestData = {
        answer: data,
      };
      instance.post(`${API_URL}/report/submit/quizSet/${reportSetId}`, requestData).catch(() => {});
    },
    [reportSetId]
  );
  const QuizComponents = [
    ...quizList.map((quiz, index) => (
      <Quiz
        key={index}
        answers={answers.current}
        setAnswers={(value) => {
          answers.current = answers.current.map((v, i) => (i === index ? value : v));
        }}
        {...quiz}
      />
    )),
    <div
      key={Infinity}
      className={styles.message}
    >
      <div>
        <div>퀴즈 종료!</div>
        <div className={styles.subMsg}>답안을 전송하고 있어요</div>
      </div>
    </div>,
  ];

  useEffect(() => {
    if (!quizList) {
      return;
    }

    interval.current = setInterval(() => {
      setStep((prev) => {
        if (prev === null) {
          return 0;
        }

        if (prev + 1 === quizList.length) {
          submit(answers.current);
        } else if (prev === quizList.length) {
          return Infinity;
        }

        return prev + 1;
      });
    }, 10 * 1000);

    return () => {
      clearInterval(interval.current);
      interval.current = null;
    };
  }, [quizList, submit]);

  useEffect(() => {
    if (step === Infinity) {
      finish();
    }
  }, [finish, step]);

  return (
    <>
      {step === null ? (
        <div className={styles.message}>
          <span>
            <Countdown seconds={10} />초 후 퀴즈를 시작합니다.
          </span>
          <LoadingIndicator />
        </div>
      ) : (
        <>
          {QuizComponents[step]}
          <div className={styles.progressBar} />
        </>
      )}
    </>
  );
}
