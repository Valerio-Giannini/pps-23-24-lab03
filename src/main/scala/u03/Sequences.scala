package u03

import u02.AnonymousFunctions.l
import u03.Optionals.Optional

object Sequences: // Essentially, generic linkedlists
  
  enum Sequence[E]:
    case Cons(head: E, tail: Sequence[E])
    case Nil()

  object Sequence:

    def sum(l: Sequence[Int]): Int = l match
     case Cons(h, t) => h + sum(t)
     case _          => 0

    def map[A, B](l: Sequence[A])(mapper: A => B): Sequence[B] = l match
      case Cons(h, t) => Cons(mapper(h), map(t)(mapper))
      case Nil()      => Nil()
    
    def filter[A](l1: Sequence[A])(pred: A => Boolean): Sequence[A] = l1 match
      case Cons(h, t) if pred(h) => Cons(h, filter(t)(pred))
      case Cons(_, t)            => filter(t)(pred)
      case Nil()                 => Nil()

    // Lab 03
    def zip[A, B](first: Sequence[A], second: Sequence[B]): Sequence[(A, B)] = first match
      case Nil()          => Nil()
      case Cons(h1, t1)   => second match
        case Cons(h2, t2) => Cons((h1, h2), zip(t1, t2))
        case Nil()        => Nil()
      
    def take[A](l: Sequence[A])(n: Int): Sequence[A] = n match
      case 0 => Nil()
      case _ => l match
        case Cons(h, t) => Cons(h , take(t)(n - 1))
        case Nil()      => Nil()
      
    def concat[A](l1: Sequence[A], l2: Sequence[A]): Sequence[A] = l1 match
      case Cons(h1, t1) => Cons(h1, concat(t1, l2))
      case Nil()        => l2 match
        case Cons(h2, t2) => Cons(h2, concat(t2, Nil()))
        case Nil()        => Nil()
    
    def flatMap[A, B](l: Sequence[A])(mapper: A => Sequence[B]): Sequence[B] = l match
      case Nil()      => Nil()
      case Cons(h, t) => concat(mapper(h), flatMap(t)(mapper))
    
    def min(l: Sequence[Int]): Optional[Int] = l match
      case Nil()          => Optional.Empty()
      case Cons(h, Nil()) => Optional.Just(h)
      case Cons(h, t)     => min(filter(l)(_ <= h))
      
    //Task 4: foldLeft 
    def foldLeft[A, B](l: Sequence[A])(v: B)(op: (B, A) => B): B = l match
      case Nil()      => v
      case Cons(h, t) => foldLeft(t)(op(v, h))(op)
  

@main def trySequences =
  import Sequences.* 
  val l = Sequence.Cons(10, Sequence.Cons(20, Sequence.Cons(30, Sequence.Nil())))
  println(Sequence.sum(l)) // 30

  import Sequence.*

  println(sum(map(filter(l)(_ >= 20))(_ + 1))) // 21+31 = 52
